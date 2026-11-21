package es.cristichi.kaiju.game;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import es.cristichi.kaiju.game.obj.Kaiju;
import es.cristichi.kaiju.game.obj.carry.Carryable;
import es.cristichi.kaiju.game.obj.carry.CarryableFactory;
import es.cristichi.kaiju.ui.KaijuGameWindow;
import es.cristichi.kaiju.ui.RewardWindow;

public class Campaign extends Thread{
    protected static final int MAX_REWARDS = 3;

    protected Thread thread;
    protected String name;
    protected String author;
    protected Game[] games;
    protected HashMap<String, Kaiju> players;
    protected CarryableFactory<? extends Carryable>[] rewardPool;

    public Campaign(String name, String author, Game[] games, HashMap<String, Kaiju> players, CarryableFactory<? extends Carryable>[] rewardPool) {
        this.name = name;
        this.author = author;
        this.games = games;
        this.players = players;
        this.rewardPool = rewardPool;
    }

    @Override
    public void run() {
        for (Game game : games) {
            game.setPlayers(players.values(), true);
            System.out.println(game.getKaijus().size());

            KaijuGameWindow window = new KaijuGameWindow(game);
            window.setSize(1600, 1000);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.startGame();

            Kaiju winner = game.waitOnGameEnd();
            window.dispose();

            players = game.getKaijus();

            if (winner != null){
                System.out.println("Winner of game: "+game.getWinner());
                
                ArrayList<CarryableFactory<? extends Carryable>> newRewards = new ArrayList<>(MAX_REWARDS);
                if (rewardPool.length <= MAX_REWARDS){
                    for (CarryableFactory<? extends Carryable> carryableFactory : rewardPool) {
                        newRewards.add(carryableFactory);
                        System.out.println("Added: "+carryableFactory.toString());
                    }
                } else {
                    int[] rewards = game.getRng().ints(0, rewardPool.length).distinct().limit(3).toArray();
                    System.out.println(rewards);
                    for (int i = 0; i < rewards.length; i++) {
                        newRewards.add(rewardPool[rewards[i]]);
                        System.out.println("Added at random: "+rewardPool[rewards[i]].toString());
                    }
                }

                RewardWindow rewardWindow = new RewardWindow(newRewards);
                rewardWindow.setSize(new Dimension(500, 200));
                rewardWindow.setLocationRelativeTo(null);
                rewardWindow.setAlwaysOnTop(true);
                rewardWindow.setVisible(true);
                CarryableFactory<? extends Carryable> item = rewardWindow.waitForChosen();
                if (item != null){
                    players.get(game.getWinner().getName()).giveItem(item.create());
                }
            } else {
                System.out.println("Winner of game: Nobody!");

                RewardWindow rewardWindow = new RewardWindow();
                rewardWindow.setSize(new Dimension(500, 200));
                rewardWindow.setLocationRelativeTo(null);
                rewardWindow.setAlwaysOnTop(true);
                rewardWindow.setVisible(true);
                rewardWindow.waitForChosen();
            }
        }
    }

    public static class Builder{
        protected String name;
        protected String author;
        protected LinkedList<Game> games;
        protected HashMap<String, Kaiju> players;
        protected LinkedList<CarryableFactory<? extends Carryable>> rewardPool;

        public Builder(String name, String author, Kaiju... players){
            this.name = name;
            this.author = author;
            this.games = new LinkedList<>();
            this.players = new HashMap<>(players.length);
            for (Kaiju player : players) {
                this.players.put(player.getName(), player);
            }
            rewardPool = new LinkedList<>();
        }

        public Builder addPlayers(Kaiju player){
            this.players.put(player.getName(), player);
            return this;
        }

        public Builder addGame(Game game){
            this.games.add(game);
            return this;
        }

        public Builder addReward(CarryableFactory<? extends Carryable> reward){
            this.rewardPool.add(reward);
            return this;
        }

        public Campaign build(){
            if (games == null){
                throw new IllegalArgumentException("Add at least a game to the Campaign.");
            }
            if (players == null){
                throw new IllegalArgumentException("Add at least a player to the Campaign.");
            }
            if (rewardPool == null){
                rewardPool = new LinkedList<>();
            }
            return new Campaign(name, author, games.toArray(new Game[games.size()]), players,
                rewardPool.toArray(new CarryableFactory<?>[rewardPool.size()]));
        }
    }
}
