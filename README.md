Application account managing.

For running:
1. start docker compose.yaml file (for starting database)
2. mvn install
3. run application;

For using:
1. There are 3 users for demonstration:
   user1, testpassword
   user2, testpassword
   user3, testpassword
   user1 has admin rights, others are clients
2. use POST http://localhost:8080/login for authentication, place in body 
   {
   "name" : "user2",
   "password":"testpassword"
   }, 
    use name and password for suitable users,
 - in response copy token from Authorization header 
 - Please use this token for suitable user in requests , put it as bearer token as authorization.  
3. GET http://localhost:8080/accounts use for all accounts check, allowed for admin only, in other cases it would be forbidden.
4. GET  http://localhost:8080/user/account/{user_id} , for example http://localhost:8080/user/account/2 , allow to check users accounts only for owner.
5. GET http://localhost:8080/account/block?accountNumber={account_number} , for example http://localhost:8080/account/block?accountNumber=1234-1234-1234 , allows admin block account by account number
6. GET http://localhost:8080/account/unblock?accountNumber={account_number} , for example http://localhost:8080/account/unblock?accountNumber=1234-1234-1234 , allows admin to unblock account by account number
7. POST http://localhost:8080/user/account/{user_id}/fill, for example http://localhost:8080/user/account/2/fill , use for filling count for user, use in body
   {
   "accountNumber": "1234-1234-1234",
   "changeAmount": 15.00
   }  
   where accountNumber is a number of account and change amount is an amount that you wish to add to your count.
8. POST http://localhost:8080/user/account/{user_id}/cash , for example http://localhost:8080/user/account/2/cash , use for taking money from the count , use body
   {
   "accountNumber": "1234-1234-1234",
   "changeAmount": 15.00
   }
   where accountNumber is a number of account and change amount is an amount that you wish to take from your count.