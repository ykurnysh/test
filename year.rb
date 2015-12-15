require 'date'
begin
	a=gets().split(' ')
	d=Date.new(a[2].to_i, a[1].to_i, a[0].to_i)
	print (d-Date.new(a[2].to_i-1, 12, 31)).to_i
end

