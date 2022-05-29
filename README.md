# Sii-internship - Maciej Lipiński

## All endpoints with example requests are avaiable after running the app at: http://localhost:8080/api/swagger-ui.html#
## Additonaly postman collection is avaiable in /src/main/resources. Included collection has example request for each endpoint.


```diff
! Polish version below/Polska wersja instrukcji znajduje się poniżej
```
English version:
If run on localhost all endpoints should start with: http://localhost:8080

- UserController handles users related actions
  * **GET** /api/user/{pageNumber}/{pageSize} - retrieves list of registered users, both path variables are optional.
in case of not passing those variables system will return all users registered into a system. When arguments are passed system will paginate the response 
into pages with chosen size. Both variables are an integer.
Example request: 
    * http://localhost:8080/api/user/ -> returns all users in one JSON
    * http://localhost:8080/api/user/0/5 -> returns first five registered users (first page with page size = 5)
  * **POST** /api/user - Allows to register new user takes body with JSON file
Example request: 
    * http://localhost:8080/api/user/
    with body:
    ```
    {
    "login": "login",
    "email": "user@email.com"
    }
    ```
  * **PATCH** /api/user/{userLogin}/{newUserEmail} - Allows to change email of registered user. takes two variables, user login and new user email.
Example request: 
    * http://localhost:8080/api/user/testlogin1/new@email.com

- LectureController handles lecture related actions
  * **GET** /api/lecture/{pageNumber}/{pageSize} -> retrieves list of lectures, both path variables are optional.
in case of not passing those variables system will return all lecture in the system. When arguments are passed system will paginate the response 
into pages with chosen size. Both variables are an integer.
Example request: 
    * http://localhost:8080/api/lecture/ -> returns all lectures in one JSON
    * http://localhost:8080/api/lecture/0/5 -> returns first five lectures (first page with page size = 5)
  * **GET** /getRegisteredLectures/{login} -> allows to fetch all lectures user is currently registered for. 
Path variable login allows to chose the user we want to query for.
Example request: 
    * http://localhost:8080/api/lecture/getRegisteredLectures/login
  * **POST** /api/lecture/{lectureId} -> Allows register user for chosen lecture. Path variable lectureId specifies wanted lecture
It also takes a body of JSON type that need to include user login and email. If the user is not already registered in the system it will automaticly create new account
Example request: 
    * http://localhost:8080/api/lecture/6
    with body:
    ```
    {
    "login": "testlogin",
    "email": "user@email.com"
    }
    ```
  * **DELETE** /api/lecture/{userLogin}/{lectureId} -> allows to cancel registration for lecture. Path variables user login specifies user login, 
lectureId specifies id of lecture we want to cancel registration for
Example request:
    * http://localhost:8080/api/lecture/testlogin1/5
  * **GET** /api/lecture/getParticipationPercentagePerLecture   -> allows to query for participation percentage of every lecture sorted by descending percentage
Example request:
    * http://localhost:8080/api/lecture/getParticipationPercentagePerLecture
  * **GET** /api/lecture/getParticipationPercentagePerSubject   -> allows to query for participation percentage of every lecture sorted by descending percentage
Example request:
    * http://localhost:8080/api/lecture/getParticipationPercentagePerSubject
    
 ---
    
Polska wersja:
W przypadku uruchomienia lokalnego należy każdy adres poprzedzić prefixem: http://localhost:8080

- UserController obejmuje akcje związane z użytkownikami
  * **GET** /api/user/{pageNumber}/{pageSize} - pobiera listę zarejestrowanych użytkowników, obie zmienne ścieżki są opcjonalne.
w przypadku nieprzekazania tych zmiennych system zwróci wszystkich użytkowników zarejestrowanych w systemie. Jeśli zostaną przekazane argumenty, system paginuje odpowiedź 
na strony o wybranym rozmiarze. Obie zmienne są liczbami całkowitymi.
Przykładowe zapytanie:
    * http://localhost:8080/api/user/ -> zwraca wszystkich użytkowników w jednym pliku JSON
    * http://localhost:8080/api/user/0/5 -> zwraca pięciu pierwszych zarejestrowanych użytkowników (pierwsza strona z rozmiarem strony = 5)
  * **POST** /api/user - Umożliwia zarejestrowanie nowego użytkownika za pomocą pliku JSON.
Przykładowe zapytanie: 
    * http://localhost:8080/api/user/
    należy uwzględnić ciało zapytania:
    ```
    {
    "login": "login",
    "email": "user@email.com"
    }
    ```
  * **PATCH** /api/user/{userLogin}/{newUserEmail} - Umożliwia zmianę adresu e-mail zarejestrowanego użytkownika. Pobiera dwie zmienne, login użytkownika i adres e-mail nowego użytkownika.
Przykładowe zapytanie: 
    * http://localhost:8080/api/user/testlogin1/new@email.com

- LectureController obsługuje czynności związane z wykładami
  * **GET** /api/lecture/{pageNumber}/{pageSize} -> pobiera listę wykładów, obie zmienne ścieżki są opcjonalne.
w przypadku nieprzekazania tych zmiennych system zwróci wszystkie wykłady w systemie. Jeśli zostaną przekazane argumenty, system paginuje odpowiedź 
na strony o wybranym rozmiarze. Obie zmienne są liczbami całkowitymi.
Przykładowe zapytanie: 
    * http://localhost:8080/api/lecture/ -> zwraca wszystkie wykłady w jednym pliku JSON
    * http://localhost:8080/api/lecture/0/5 -> zwraca pięć pierwszych wykładów (pierwsza strona z rozmiarem strony = 5)
  * **GET** /getRegisteredLectures/{login} -> pozwala na pobranie wszystkich wykładów, na które użytkownik jest aktualnie zarejestrowany. 
Zmienna ścieżkowa login pozwala wybrać użytkownika, którego chcemy zapytać.
Przykładowe zapytanie: 
    * http://localhost:8080/api/lecture/getRegisteredLectures/login
  * **POST** /api/lecture/{lectureId} -> Umożliwia zarejestrowanie użytkownika na wybrany wykład. Zmienna ścieżkowa lectureId określa poszukiwany wykład.
Pobiera również treść typu JSON, która musi zawierać login i email użytkownika. Jeśli użytkownik nie jest jeszcze zarejestrowany w systemie, automatycznie zostanie utworzone nowe konto.
Przykładowe zapytanie: 
    * http://localhost:8080/api/lecture/6
     należy uwzględnić ciało zapytania:
    ```
    {
    "login": "testlogin",
    "email": "user@email.com"
    }
    ```
  * **DELETE** /api/lecture/{userLogin}/{lectureId} -> umożliwia anulowanie rejestracji na wykład. Zmienne path login user określają login użytkownika, 
lectureId określa id wykładu, na który chcemy anulować rejestrację
Przykładowe zapytanie: 
    * http://localhost:8080/api/lecture/testlogin1/5
  * **GET** /api/lecture/getParticipationPercentagePerLecture   -> umożliwia zapytanie o procentowy udział w każdym wykładzie posortowany według największego procentu
Przykładowe zapytanie: 
    * http://localhost:8080/api/lecture/getParticipationPercentagePerLecture
  * **GET** /api/lecture/getParticipationPercentagePerSubject   -> umożliwia zapytanie o procentowy udział w każdym wykładzie posortowany według największego procentu
Przykładowe zapytanie: 
    * http://localhost:8080/api/lecture/getParticipationPercentagePerSubject
    
