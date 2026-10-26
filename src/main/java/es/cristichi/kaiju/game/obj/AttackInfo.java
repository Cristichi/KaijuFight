package es.cristichi.kaiju.game.obj;

import es.cristichi.kaiju.game.GameObject;

public record AttackInfo(AttackType type, Position objPos, GameObject objGameObject){
}