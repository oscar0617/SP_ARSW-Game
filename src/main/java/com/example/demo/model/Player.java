package com.example.demo.model;

/**
 * Represents a player in a game or application context.
 * Each player is identified by their room ID, nickname, and player ID.
 */
public class Player {

    private String idRoom;
    private String nickName;
    private String idPlayer;

    /**
     * Constructs a new Player with the specified room ID, nickname, and player ID.
     *
     * @param idRoom the ID of the room the player belongs to.
     * @param nickName the nickname of the player.
     * @param idPlayer the unique ID of the player.
     */
    public Player(String idRoom, String nickName, String idPlayer) {
        this.idRoom = idRoom;
        this.nickName = nickName;
        this.idPlayer = idPlayer;
    }

    /**
     * Default constructor for creating an empty Player instance.
     */
    public Player() {}

    /**
     * Gets the ID of the room the player belongs to.
     *
     * @return the ID of the room.
     */
    public String getIdRoom() {
        return idRoom;
    }

    /**
     * Sets the ID of the room the player belongs to.
     *
     * @param idRoom the new room ID.
     */
    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    /**
     * Gets the nickname of the player.
     *
     * @return the nickname of the player.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets the nickname of the player.
     *
     * @param nickName the new nickname of the player.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Gets the unique ID of the player.
     *
     * @return the unique player ID.
     */
    public String getIdPlayer() {
        return idPlayer;
    }

    /**
     * Sets the unique ID of the player.
     *
     * @param idPlayer the new unique player ID.
     */
    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }
}