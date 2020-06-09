
1. Create a MySQL db:
```
create database transaction01;
use transaction01;
```

2. Create a table:
```
CREATE TABLE `account` (
  `accountId` bigint(19) NOT NULL AUTO_INCREMENT,
  `accountNumber` varchar(100) DEFAULT NULL,
  `iban` varchar(100) DEFAULT NULL,
  `cnp` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `currentBalance` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`accountId`)
);
```

3. Create another table:
```
CREATE TABLE `transaction` (
  `transactionId` bigint(19) NOT NULL AUTO_INCREMENT,
  `transactionType` varchar(100) DEFAULT NULL,
  `cnp` varchar(100) DEFAULT NULL,
  `transactionAmount` decimal(10,2) DEFAULT NULL,
  `payment` tinyint(1) NOT NULL DEFAULT '0',
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`transactionId`)
);
```

4. Insert some values:
```
insert into account (accountNumber, iban, cnp, name, currentBalance) 
values ('41230192109218940921875412', 'GR96 0810 0010 0000 0123 45', '1880724190281', 'Ion Ionescu', 9908);

insert into account (accountNumber, iban, cnp, name, currentBalance) 
values ('10982738574984850120958235', 'SA89 3704 0044 0532 0130 00', '1831222132132', 'John Book', 13766);

insert into account (accountNumber, iban, cnp, name, currentBalance) 
values ('73847120498549304812908475', 'FR91 1000 0000 0123 4567 89', '2761031799173', 'Bill Black', 15670);
```

5. Add your MySQL credentials in the transactions-persistence microservice in application.properties
```
spring.datasource.username = root
spring.datasource.password = parola
```

6. Start ActiveMQ on your machine (The app uses JMS and ActiveMQ for asynchorous communication)

7. Start transactions-validations and transactions-persistence microservices
transactions-validations uses port 8080 and transactions-persistence uses port 8090

8. You can use these endpoints for testing:
```
POST request: http://localhost:8080/ibantoiban
{
    "fromIban": "SA89 3704 0044 0532 0130 00",
    "toIban": "FR91 1000 0000 0123 4567 89",
    "amount": "850",
    "description": "Payment from John's iban to Bill's iban"
}

POST request: http://localhost:8080/ibantowallet
{
    "fromIban": "FR91 1000 0000 0123 4567 89",
    "toWalletNumber": "41230192109218940921875412",
    "amount": "1000",
    "description": "Payment from Bill's iban to Ion's wallet"
}

POST request: http://localhost:8080/wallettoiban
{
    "fromWalletNumber": "41230192109218940921875412",
    "toIban": "FR91 1000 0000 0123 4567 89",
    "amount": "1500",
    "description": "Payment from Ion's wallet to Bill's iban"
}

POST request: http://localhost:8080/wallettowallet
{
    "fromWalletNumber": "73847120498549304812908475",
    "toWalletNumber": "10982738574984850120958235",
    "amount": "500",
    "description": "Payment from Bill's wallet to John's wallet"
}

POST request: http://localhost:8090/pdfreport
{
    "cnp": "1831222132132"
}
```

The pdf raport will be downloaded on the machine

Example of raport:
```
1. Nume: John Book
2. CNP: 1831222132132
3. IBAN: SA89 3704 0044 0532 0130 00
Tranzactii:
1. IBAN_TO_IBAN | 7 tranzactii | -2.500 RON
    a. Detalii tranzactia 1
      - Plata: 1.000
      - Descriere: Payment from John's iban to Ana's iban
    b. Detalii tranzactia 2
      - Incasare: 1.500
      - Descriere: Payment from Ion's iban to John's iban
    c. Detalii tranzactia 3
      - Incasare: 800
      - Descriere: Payment from Ana's iban to John's iban
    d. Detalii tranzactia 4
      - Plata: 750
      - Descriere: Payment from John's iban to Ion's iban
    e. Detalii tranzactia 5
      - Plata: 1.950
      - Descriere: Payment from John's iban to Ana's iban
    f. Detalii tranzactia 6
      - Plata: 850
      - Descriere: Payment from John's iban to Ana's iban
    g. Detalii tranzactia 7
      - Plata: 250
      - Descriere: Payment from John's iban to Ana's iban
2. IBAN_TO_WALLET | 3 tranzactii | -700 RON
    a. Detalii tranzactia 1
      - Plata: 1.000
      - Descriere: Payment from John's iban to Ion's wallet
    b. Detalii tranzactia 2
      - Plata: 900
      - Descriere: Payment from John's iban to Ana's wallet
    c. Detalii tranzactia 3
      - Incasare: 1.200
      - Descriere: Payment from Ana's iban to John's wallet
3. WALLET_TO_IBAN | 3 tranzactii | 1.400 RON
    a. Detalii tranzactia 1
      - Plata: 1.800
      - Descriere: Payment from John's wallet to Ana's iban
    b. Detalii tranzactia 2
      - Incasare: 2.100
      - Descriere: Payment from Ion's wallet to John's iban
    c. Detalii tranzactia 3
      - Incasare: 1.100
      - Descriere: Payment from Ion's wallet to John's iban
4. WALLET_TO_WALLET | 9 tranzactii | 1.060 RON
    a. Detalii tranzactia 1
      - Plata: 1.000
      - Descriere: Payment from John's wallet to Ana's wallet
    b. Detalii tranzactia 2
      - Plata: 600
      - Descriere: Payment from John's wallet to Ion's wallet
    c. Detalii tranzactia 3
      - Incasare: 1.400
      - Descriere: Payment from Ana's wallet to John's wallet
    d. Detalii tranzactia 4
      - Incasare: 870
      - Descriere: Payment from Ana's wallet to John's wallet
    e. Detalii tranzactia 5
      - Plata: 870
      - Descriere: Payment from John's wallet to Ana's wallet
    f. Detalii tranzactia 6
      - Plata: 870
      - Descriere: Payment from Gigi's wallet to Ana's wallet
    g. Detalii tranzactia 7
      - Plata: 870
      - Descriere: Payment from John's wallet to Ana's wallet
    h. Detalii tranzactia 8
      - Incasare: 2.500
      - Descriere: Payment from Ana's wallet to John's wallet
    i. Detalii tranzactia 9
      - Incasare: 500
      - Descriere: Payment from Ana's wallet to John's wallet
```

Informations abut project:
- transactions-validation is for validation, for example the regex pattern accept this kind of iban:
SA89 3704 0044 0532 0130 00, FR91 1000 0000 0123 4567 89 and cnp like this: 1831222132132, 1880724190281
- transactions-persistence is for persistence and also it can generate a statement report for every user,
and for every transaction 2 rows are created in the database, one for payer cnp and one for payee cnp
- The communication between microservices is asynchronous using JMS and ActiveMQ broker
- The implementation for recovery mechanism when a microservice is temporary unavailable is Histrix CircuitBreaker, 
but it isn't used because I think it isn't necesary because the communication between microservices is asynchoronous
and transactions-validation microservice send the message to the queue and transactions-persistence get the message
from the queue and if transactions-persistence is temporary unavailable the message is waiting on the queue until
transactions-persistence microservice is available.
- To retrieve the pdfreport, I used a POST request because I think cnp is a sensible information and it shouldn't 
appear in the endpoint.
- there are some unit tests
