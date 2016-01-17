require 'uri'
class Document < ActiveRecord::Base
attr_accessor :title, :content
after_initialize :reset
    def reset
	@text=""
	@bold=Array.new
	@italic=Array.new
	@underlined=Array.new
	@striked=Array.new
	@alignment=[0]
	@indent=[0]
	@font=Hash.new
	@color=Hash.new
	@tables=Array.new
	end
	def oper!(a, b, c)
	    i=0
	    while(i<c.length && c[i].max<a) do
		i=i+1
	    end
	    if(i==c.length) then
		c=c+[(a..b)];
		return c
	    elsif(c[i].max==b && c[i].min==a) then
		c.delete_at(i);
	    elsif(c[i].max>b && c[i].min==a) then
		c[i]=(b..c[i].max);
	    elsif(c[i].max==b && c[i].min<a) then
		c[i]=(c[i].min..a);
	    elsif(c[i].max<b && c[i].min==a) then
		c[i]=(c[i].max..b);
	    elsif(c[i].max==b && c[i].min>a) then
		c[i]=(a..c[i].min);
	    elsif(c[i].min>b)
		c.insert(i, (a..b));
	    elsif(c[i].max>b && c[i].min<a) then
		tmp=c[i].max
		c[i]=(c[i].min..a-1)
		c.insert(i+1, (b+1..tmp))
	    elsif(c[i].max>b && c[i].min>a && c[i-1].max<a-1) then
		tmp=c[i].max
		c[i]=(a..c[i].min-1)
		c.insert(i+1, (b+1..tmp))
	    elsif(c[i].max>b && c[i].min>a && c[i-1].max==a-1) then
		c[i]=(c[i].min..a-1)
	    elsif(c[i].max<b && c[i].min<a && (i==c.length-1 || c[i+1].min>b+1)) then
		c.insert(i+1, (c[i].max+1..b))
		tmp=c[i].min
		c[i]=(tmp-1..a-1)
	    elsif(c[i].max<b && c[i].min>a && (i==c.length-1 || c[i+1].min>b+1)) then
		tmp=c[i].max
		c[i]=(a..c[i].min-1)
		c.insert(i+1, (tmp+1..b))
	    elsif(c[i].max<b && c[i].min>a && (i==c.length-1 || c[i+1].min==b+1)) then
		c[i]=(b+1..c[i].max)
	    end
	    return c
	end
	def setBold(a, b)
		@bold=oper!(a, b, @bold)
	end
	def setItalic(a, b)
		@italic=oper!(a, b, @italic)
	end
	def setUnderlined(a, b)
		@underlined=oper!(a, b, @underlined)
	end
	def setStriked(a, b)
		@striked=oper!(a, b, @striked)
	end
	def operParagraph!(value, b, c)
	    paragraph=0
	    (0..b).each{|x|
		if(@text[x]=="\n") then
		    paragraph=paragraph+1
		end
	    }
	    c[paragraph]=value
	    return c
	end
	def setAlignment(value, b)
		@alignment=operParagraph!(value, b, @alignment)
	end
	def setIndent(value, b)
	    @indent=operParagraph!(value, b, @indent)
	end
	def setColor(value, a, b)
	end
	def setFont(value, a, b)
	end
	def addTable(x, y, c)
		(0..x*y).each{|z| @text.insert(c, 7.chr)}
		@tables=@tables+[Array.new(x, Array.new(y))]
		@tables.map{|x| x.map{|y| y=false}}
	end
	def addColumn(c, i)
		@tables[c].insert(i, Array.new)
	end
	def addRow(c, i)
		@tables[c].each{|x| x.insert(i, false)}
	end
	def insert(s, c)
		s=URI.unescape(s)
	    paragraph=0
	    (0..c).each{|x|
		if(@text[x]=="\n") then
		    paragraph=paragraph+1
		end
		}
		(0..s.length-1).reverse_each{|x| @text.insert(c, s[x])
		    if(s[x]=="\n") then
			@alignment.insert(paragraph, @alignment[paragraph])
			@indent.insert(paragraph, @indent[paragraph])
		    end
		}
		[@bold, @italic, @underlined, @striked].each{|x|
			(0..x.length-1).each{|y|
				if(x[y].end>=c) then
					x[y]=(x[y].begin..x[y].end+s.length)
				end
				if(x[y].begin>=c) then
					x[y]=(x[y].begin+s.length..x[y].end)
				end
			}
		}
	end
	def deletebefore(l, c)
		paragraph=0
	    (0..c).each{|x|
		if(@text[x]=="\n") then
		    paragraph=paragraph+1
		end
		}
		(0..l-1).reverse_each{|x| 
		    if(@text[c-l]=="\n") then
			@aligment[paragraph].delete_at(paragraph)
			@indent[paragraph].delete_at(paragraph)
		    end
		@text=(c-l==0?"":@text[0..c-l-1])+@text[c-l+1..@text.length-1]}
		[@bold, @italic, @underlined, @striked].each{|x|
			(0..x.length-1).each{|y|
				if(x[y].begin>=c-l) then
					x.delete_at(y)
				end
			}
			(0..x.length-1).each{|y|
				if(x[y].end>=c) then
					x[y]=(x[y].begin..x[y].end-l)
				end
				if(x[y].begin>=c) then
					x[y]=(x[y].begin-l..x[y].end)
				end
			}
		}
	end
	def deleteafter(l, c)
		paragraph=0
	    (0..c).each{|x|
		if(@text[x]=="\n") then
		    paragraph=paragraph+1
		end
		}
		(0..l-1).reverse_each{|x|
		    if(@text[c]=="\n") then
			@aligment[paragraph].delete_at(paragraph)
			@indent[paragraph].delete_at(paragraph)
		    end
		 @text=(c==0?"":@text[0..c-1])+@text[c+1..@text.length-1]}
		[@bold, @italic, @underlined, @striked].each{|x|
			(0..x.length-1).each{|y|
				if(x[y].end<=c+l) then
					x.delete_at(y)
				end
			}
			(0..x.length-1).each{|y|
				if(x[y].end>=c) then
					x[y]=(x[y].begin..x[y].end-l)
				end
				if(x[y].begin>=c) then
					x[y]=(x[y].begin-l..x[y].end)
				end
			}
		}
	end
	def serialize
	    @content=Marshal.dump([@text, @bold, @italic, @underlined, @striked, @alingment, @indent, @font, @color, @tables])
	end
	def deserialize
		@text, @bold, @italic, @underlined, @striked, @alingment, @indent, @font, @color, @tables=Marshal.load(@content)
	end
	def html
	    paragraph=0
	    table=0
	    column=0
	    row=0
	    result="<p align=\""+["left", "center", "right"][@alignment[0]]+"\" style=\'margin-left:"+@indent[0].to_s+"cm\'>"
	    (0..@text.length-1).each{|x|
		if(@bold.any?{|y| y.min==x}) then
		    result=result+="<b>"
		end
		if(@italic.any?{|y| y.min==x}) then
		    result=result+="<i>"
		end
		if(@underlined.any?{|y| y.min==x}) then
		    result=result+="<u>"
		end
		if(@striked.any?{|y| y.min==x}) then
		    result=result+="<strike>"
		end
		if(@text[x]=="\n") then
			paragraph=paragraph+1
			result=result+="</p><p align=\""+["left", "center", "right"][@alignment[paragraph]]+"\" style=\'margin-left:"+@indent[paragraph].to_s+"cm\'>"
		end
		if(@text[x]==7.chr) then
		if(column==0 && row==0) then
		    result=result+"<table border><tr><td>"
			column=1
		elsif(row==@tables[table].length-1 && column==@tables[table][row].length) then
		    result=result+"</td></tr></table>"
		    table=table+1
		    row=0
		    column=0
		elsif(column==@tables[table][row].length) then
		    column=1
		    row=row+1
		    result=result+"</td></tr><tr><td>"
		else
			result=result+"</td><td>"
			column=column+1
		end
		else
		result=result+@text[x]
		end
		if(@bold.any?{|y| y.max==x}) then
		    result=result+="</b>"
		end
		if(@italic.any?{|y| y.max==x}) then
		    result=result+="</i>"
		end
		if(@underlined.any?{|y| y.max==x}) then
		    result=result+="</u>"
		end
		if(@striked.any?{|y| y.max==x}) then
		    result=result+="</strike>"
		end
	    }
	result=result+"</p>"
	end
end
