begin
	h={}
	vowels=['a', 'e', 'i', 'o', 'u', 'y']
	"a".each_byte{|x| (0..25).each{|y| c=(x+y).chr 
if(vowels.any?{|z| z==c}) then h[c]=y+1 end
}}
	print h
end