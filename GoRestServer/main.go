package main

import (
	"database/sql"
	"log"
	"net/http"

	"github.com/gin-gonic/gin"
	_ "github.com/mattn/go-sqlite3"
)

type user struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

func connectToDatabase() (*sql.DB, error) {
	db, err := sql.Open("sqlite3", "./database.db")
	if err != nil {
		return nil, err
	}
	return db, nil
}


func getUsers(c *gin.Context) {

	db, err := connectToDatabase()
	if err != nil {
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}
	defer db.Close()

	rows, err := db.Query("SELECT username, password FROM users")
	if err != nil {
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}
	defer rows.Close()

	var users []user
	for rows.Next() {
		var u user
		err := rows.Scan(&u.Username, &u.Password)
		if err != nil {
			log.Fatal(err)
			c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
			return
		}
		users = append(users, u)
	}

	c.IndentedJSON(http.StatusOK, users)
}

func postUsers(c *gin.Context) {
	db, err := connectToDatabase()
	if err != nil {
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}
	defer db.Close()

	var newUser user
	if err := c.BindJSON(&newUser); err != nil {
		c.IndentedJSON(http.StatusBadRequest, gin.H{"message": "invalid request"})
		return
	}

	stmt, err := db.Prepare("INSERT INTO users (username, password) VALUES (?, ?)")
	if err != nil {
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}
	defer stmt.Close()

	_, err = stmt.Exec(newUser.Username, newUser.Password)
	if err != nil {
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}

	c.IndentedJSON(http.StatusCreated, newUser)
}

func getUserByUsername(c *gin.Context) {
	username := c.Param("username")

	db, err := connectToDatabase()
	if err != nil {
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}
	defer db.Close()

	var u user
	err = db.QueryRow("SELECT username, password FROM users WHERE username = ?", username).Scan(&u.Username, &u.Password)
	if err != nil {
		if err == sql.ErrNoRows {
			c.IndentedJSON(http.StatusNotFound, gin.H{"message": "user not found"})
			return
		}
		log.Fatal(err)
		c.IndentedJSON(http.StatusInternalServerError, gin.H{"message": "internal server error"})
		return
	}

	c.IndentedJSON(http.StatusOK, u)
}

func main() {
	db, err := connectToDatabase()
   	if err != nil {
   		log.Fatal(err)
   	}
   	defer db.Close()

   	createTableSQL := `
   		CREATE TABLE IF NOT EXISTS users (
   			username TEXT NOT NULL,
   			password TEXT NOT NULL
   		)`

   	_, err = db.Exec(createTableSQL)
   	if err != nil {
   		log.Fatal(err)
   	}

	router := gin.Default()
	router.GET("/users", getUsers)
	router.POST("/users", postUsers)
	router.GET("/users/:username", getUserByUsername)

	//router.Run("localhost:8080")
    router.Run("192.168.0.15:8080")
}
