begin
	h={}
	s=gets()
	while(s!="stop\r\n")
		a=s.split(' ')
		if(a.length<3)
			print("Wrong string\n")
		else
			key=""
			(0..a.length-3).each{|z| key=key+a[z]+" "}
			h[key[0, key.length-1]]={}
			h[key[0, key.length-1]][a[a.length-2].to_f]=a[a.length-1].to_f
		end
		s=gets()
	end
	print h
	print "\n\n"
	hh={}
	h.each{|x, y| hh[x]=y.keys[0]*y.values[0]}
	print hh
	print "\n\n"
	print hh.values.reduce(:+)
end

