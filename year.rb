require 'date'
begin
	a=gets().split(' ')
	b=[31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
	if(a[1].to_i>1) then x=b[0..a[1].to_i-2].reduce(:+)+a[0].to_i else x=a[0] end
	if(a[1].to_i>=3 && ((a[2].to_i%4==0 && a[2].to_i%100!=0) || a[2].to_i%400==0)) then x=x+1 end
	print x
end

