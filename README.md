# spotify-to-yandex
Импортер данных из Spotify в Яндекс.Музыку. Написан на Clojure с поддержкой командной строки

Для работы необходим Bearer токен    
Взять можно [тут](https://developer.spotify.com/console/get-current-user-saved-tracks) обязательно с правами `user-library-read`

Для REPL среды нужно инициализировать переменную с токеном вместо nil  
`(def bearer nil)`

Для работы через cli нужно добавить токен в deps алиас вместо пустой строки  
`{:main-fn {:exec-fn   main/main
            :exec-args {:berear- ""}}}`
            
Запуск в консоли происходит через вызов алиаса  
`clj -X:main-fn`  
Функция `main` сама подцепит данные и создаст документ.

На выходе будет документ `spotify.txt`, в формате Автор - Название - Альбом
