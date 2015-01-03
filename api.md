## Login ##

curl -X POST 'http://127.0.0.1:8080/expense-tracker/api/login' -d 'username=zjor&password=Gfhjkm1' -v

## Create Expense ##

curl -X PUT 'http://127.0.0.1:8080/expense-tracker/api/expenses' -d 'description=coffee&amount=15&timestamp=1000' --cookie "JSESSIONID=8F88BE6574EAA4BBC26F5B8B50F6701C; Path=/expense-tracker/; HttpOnly" -v