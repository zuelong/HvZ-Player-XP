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
public class Mission {
    public String name;
    public String desc;
    public Integer status;
    public Integer xp;
    public Integer players;
    
    public Mission(String name, Integer players, Integer xp, String desc){
        this.name = name;
        this.players = players;
        this.xp = xp;
        this.status = 0;
        this.desc = desc;
    }
}
