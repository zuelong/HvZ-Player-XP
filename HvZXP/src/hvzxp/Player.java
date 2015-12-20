/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvzxp;

/**
 *
 * @author Aaron
 */
public class Player {
    public String name;
    public String id;
    public Integer xp;
    
    public Player(String id, String name, Integer xp){
        this.id = id;
        this.name = name;
        this.xp = xp;
    }
}
