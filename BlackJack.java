package blackjack;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;


public class BlackJack extends PApplet {
	Card[] deck = new Card[52];
	Random rand = new Random();
	int numplayers = 2;
	int deckTracker = 0;
	Player player1 = new Player(); 
	Player dealer = new Player();
	float length, side, border;
	float xHit, yHit, xStay, yStay, bWidth, bLength;
	float xRetry, yRetry, rWidth, rLength;
	boolean hitPressed = false, stayPressed = false, retryPressed = false;
	int state = 0;
	int timer;
	
	
	public void setup() {
		size(displayWidth, displayHeight);
		
		length = (float)((1/8.0)*height);
		side = (float)((9/14.0)*length);
		border = (float) (height/120.0);
		
		background(255,255,255);
		
		createDeck();
		shuffleDeck();
		player1.hit(); player1.hit();
		dealer.hit(); dealer.hit();
	}

	public void draw() {
		background(255,255,255);
		drawTable();
		drawDeck();
		drawHead((float)(1/2.0)*width, (float)(1/8.0)*height);
		drawHead((float)(1/2.0)*width, (float)(7/8.0)*height);
		
		drawHand((float)(35/100.0)*width,(float)((3/4.0 - 1/8.0 )*height - border), player1, true);
		
		if(state == 0){
			drawHand((float)(35/100.0)*width,(float)((1/4.0)*height+border), dealer, false);
			drawButtons();
			if(player1.bust){
				timer = millis();
				delay(200);
				state = 2;
			}
		}
		if(state == 1){//Player stayed
			drawHand((float)(35/100.0)*width,(float)((1/4.0)*height+border), dealer, true);
			if(millis() - timer > 500){
				if(dealer.val > 16){
					state = 3;
				}
				else{
					dealer.hit();
					timer = millis();
				}
			}
		}
		if(state == 2){//Player busted
			drawHand((float)(35/100.0)*width,(float)((1/4.0)*height+border), dealer, true);
			float textS = border*10;
			textSize(textS);
			fill(0, 0, 0);
			text("DEALER", (float)((1/2.0)*width - textS*4 - (1/16.0)*height), (float)( (1/16.0)*height), textS*7, textS*2);
			text("WINS", (float)((1/2.0)*width + (1/16.0)*height + border), (float)( (1/16.0)*height), textS*6, textS*2);
			text("YOU", (float)((1/2.0)*width - textS*2.2 - (1/16.0)*height), (float)( (3/4.0 + 1/16.0)*height), textS*7, textS*2);
			text("BUST", (float)((1/2.0)*width + (1/16.0)*height + border ), (float)( (3/4.0 + 1/16.0)*height), textS*6, textS*2);
			drawRetry();
		}
		if(state == 3){//Dealer and Player done
			drawHand((float)(35/100.0)*width,(float)((1/4.0)*height+border), dealer, true);
			fill(0,0,0);
			float textS = border*10;
			textSize(textS);
			if(dealer.bust){
				text("DEALER", (float)((1/2.0)*width - textS*4 - (1/16.0)*height), (float)( (1/16.0)*height), textS*7, textS*2);
				text("BUSTS", (float)((1/2.0)*width + (1/16.0)*height + border), (float)( (1/16.0)*height), textS*6, textS*2);
				text("YOU", (float)((1/2.0)*width - textS*2.2 - (1/16.0)*height), (float)((3/4.0 +1/16.0)*height), textS*7, textS*2);
				text("WIN", (float)((1/2.0)*width + (1/16.0)*height + border ), (float)( (3/4.0 + 1/16.0)*height), textS*6, textS*2);
			}
			else if(player1.val > dealer.val){
				text("DEALER", (float)((1/2.0)*width - textS*4 - (1/16.0)*height), (float)( (1/16.0)*height), textS*7, textS*2);
				text("LOSES", (float)((1/2.0)*width + (1/16.0)*height + border), (float)( (1/16.0)*height), textS*6, textS*2);
				text("YOU", (float)((1/2.0)*width - textS*2.2 - (1/16.0)*height), (float)( (3/4.0 + 1/16.0)*height), textS*7, textS*2);
				text("WIN", (float)((1/2.0)*width + (1/16.0)*height + border ), (float)( (3/4.0 + 1/16.0)*height), textS*6, textS*2);
			}
			else{
				text("DEALER", (float)((1/2.0)*width - textS*4 - (1/16.0)*height), (float)( (1/16.0)*height), textS*7, textS*2);
				text("WINS", (float)((1/2.0)*width + (1/16.0)*height + border ), (float)( (1/16.0)*height), textS*6, textS*2);
				text("YOU", (float)((1/2.0)*width - textS*2.2 - (1/16.0)*height), (float)( (3/4.0 + 1/16.0)*height), textS*7, textS*2);
				text("LOSE", (float)((1/2.0)*width + (1/16.0)*height + border ), (float)( (3/4.0 + 1/16.0)*height), textS*6, textS*2);
			}
			drawRetry();
		}
	}
	public void mousePressed(){
		if(state == 0){
			if(mouseX > xHit && mouseX < xHit+bWidth){
				if(mouseY > yHit && mouseY < yHit+bLength){
					hitPressed = true;
				}
			}
			else if(mouseX > xStay && mouseX < xStay+bWidth){
				if(mouseY > yStay && mouseY < yStay+bLength){
					stayPressed = true;
				}
			}
		}
		if(state == 2 || state == 3){
			if(mouseX > xRetry && mouseX < xRetry+rWidth){
				if(mouseY > yRetry && mouseY < yRetry+rLength){
					retryPressed = true;
				}
			}
		}
	}
	public void mouseReleased(){
		if(state == 0){
			if(mouseX > xHit && mouseX < xHit+bWidth){
				if(mouseY > yHit && mouseY < yHit+bLength){
					if(hitPressed){
						player1.hit();
						hitPressed = false;
					}
				}
			}
			else if(mouseX > xStay && mouseX < xStay+bWidth){
				if(mouseY > yStay && mouseY < yStay+bLength){
					if(stayPressed){
						state = 1;
						stayPressed = false;
						timer = millis();
					}
				}
			}
			else{
				hitPressed = false; stayPressed = false;
			}
		}
		if(state == 2 || state == 3){
			if(mouseX > xRetry && mouseX < xRetry+rWidth){
				if(mouseY > yRetry && mouseY < yRetry+rLength){
					if(retryPressed){
						reset();
						retryPressed = false;
					}
				}
			}
			else{
				retryPressed = false;
			}
		}
	}
	public void reset(){
		player1.bust = false; dealer.bust = false;
		player1.hand.clear(); dealer.hand.clear();
		shuffleDeck();
		deckTracker = 0;
		for(int i = 0; i < deck.length; i++){//resets any Aces picked back to value 11
			if(deck[i].face == "A"){
				deck[i].val = 11;
			}
		}
		state = 0;
		player1.hit(); player1.hit();
		dealer.hit(); dealer.hit();
	}

	public void drawButtons(){
		bWidth = (float)(1/5.0*height);
		bLength = (float)(1/8.0*height);
		//x and y of Hit Button
		xHit = (float)( (1/2.0)*width - bWidth - (1/16.0)*width );
		yHit = (float)( (3/4.0+1/16.0) *height);
		//x and y of Stay Button
		xStay = (float)( (1/2.0)*width + (1/16.0)*width );
		yStay = (float)( (3/4.0+1/16.0) *height);
		
		fill(71,214,55);
		rect(xHit,yHit,bWidth,bLength);
		fill(221,24,24);
		rect(xStay, yStay, bWidth, bLength);
		
		float textS = border*10;
		if(hitPressed){
			fill(71,214,55);
		}
		else{
			fill(255,255,255);
		}
		rect(xHit+border, yHit+border, bWidth - 2*border, bLength - 2*border);
		if(stayPressed){
			fill(221,24,24);
		}
		else{
			fill(255,255,255);
		}
		rect(xStay+border, yStay+border, bWidth - 2*border, bLength - 2*border);
		
		textSize(textS);
		fill(0,0,0);
		text("Hit", (float)(xHit+bWidth/2.0 - textS*0.75), (float)(yHit+bLength/2.0-(3/5.0)*textS), bWidth, bLength);//0.75 works
		text("Stay", (float)(xStay+bWidth/2.0 - textS), (float)(yStay+bLength/2.0 - (3/5.0)*textS),bWidth ,bLength);
	}
	public void drawRetry(){
		rWidth = (float)(1/3.0*height);
		rLength = (float)(1/8.0*height);
		xRetry = (float)(1/2.0*width - rWidth/2.0);
		yRetry = (float)(1/2.0*height - rLength/2.0);
		
		fill(160,152, 154);
		rect(xRetry, yRetry, rWidth, rLength);
		if(retryPressed){
			fill(160,152, 154);
		}
		else{
			fill(255, 255, 255);
		}
		rect(xRetry+border, yRetry+border, rWidth - 2*border, rLength - 2*border);
		fill(0,0,0);
		float textS = border*10;
		textSize(textS);
		text("Retry?", (float)( 1/2.0*width - textS*1.5), (float)(yRetry+rLength/2.0 - (3/5.0)*textS),rWidth ,rLength);
	}

	public void drawTable(){
		fill(73, 54, 33);
		rect((float)(1/4.0)*width - 2*border ,(float)(1/4.0)*height - 2*border, (float)(1/2.0)*width + 4*border, (float)(1/2.0)*height + 4*border);
		fill(40,73, 39);
		rect((float)(1/4.0)*width ,(float)(1/4.0)*height, (float)(1/2.0)*width, (float)(1/2.0)*height);
	}
	public void drawHead(float x, float y){
		fill(160,152, 154);
		ellipse(x, y, (float)(height*(1/8.0)), (float)(height*(1/8.0)) );
	}
	public void drawDeck(){
		length = (float)((1/8.0)*height);
		side = (float)((9/14.0)*length);
		border = (float) (height/120.0);
		
		float x = (float)(width/2.0 - side/2.0 + border/2.0);
		float y = (float)(height/2.0 - length/2.0 + border/2.0);
		for(int i = 0; i < 3; i++){
			drawCardBack(x, y);
			x -= border/2.0;
			y -= border/2.0;
		}
		
	}
	class Card{
		//in unicode
		// \u2660 is spade \u2665 is heart \u2663 is club \u25c6 is diamond
		int val;
		String face;
		String suit;
	}
	class Player{
		ArrayList<Integer> hand = new ArrayList<Integer>();
		int val;
		boolean bust;
		Player(){
			this.val = 0;
			bust = false;
		}
		public void hit(){
			this.hand.add(deckTracker); deckTracker++;
			this.sumHand();
		}
		public void sumHand(){
			this.val = 0;
			for(int i = 0; i < hand.size(); i++){
				val += deck[this.hand.get(i).intValue()].val;
			}
			if(this.val > 21){//changes Ace from 11 to 1
				for(int i = 0; i < hand.size(); i++){
					if(deck[this.hand.get(i).intValue()].val == 11){
						deck[this.hand.get(i).intValue()].val = 1;
						val -= 10;
					}
				}
			}
			if(this.val > 21){//if still higher than 21 bust
				bust = true;
			}
		}
		
	}
	public void drawCard(float x, float y, Card card){
		length = (float)((1/8.0)*height);
		side = (float)((9/14.0)*length);
		border = (float) (height/120.0);
		fill(76, 98, 178);//blue border
		rect(x , y, side, length);
		fill(255, 255, 255);
		rect(x+border, y+border, side - (2*border), length - (2*border) );
		textSize(border*3);
		if(card.suit == "\u2660" || card.suit == "\u2663"){
			fill(0, 0, 0);
		}
		if(card.suit == "\u2665" || card.suit == "\u25c6"){
			fill(173,34,43);
		}
		text(card.suit+card.face, (float)(x+1.5*border), y+5*border, border*8, border*4 );
		
	}
	public void drawCardBack(float x, float y){
		fill(0,0,0);
		
		rect(x , y, side, length);
		fill(76, 98, 178);
		rect(x+border, y+border, side - (2*border), length - (2*border) );
	}
	public void drawHand(float x, float y, Player player, boolean disp){
		for(int i = 0; i < player.hand.size(); i++){
			if(i == 0 && !disp){
				drawCardBack(x,y);
			}
			else{
				drawCard(x, y, deck[player.hand.get(i).intValue()]);
			}
			x += side + border;
		}
	}
	
	public void createDeck(){
		for(int i = 0; i < deck.length; i++){
			deck[i] = new Card();
			if(i%13 == 0){//Ace
				deck[i].val = 11;
				deck[i].face = "A";
			}
			else if(i%13 == 1){//Jack
				deck[i].val = 10;
				deck[i].face = "J";
			}
			else if(i%13 <= 10 && i%13 > 1){//number cards 2 - 10
				deck[i].val = (i%13);
				deck[i].face = Integer.toString(i%13);
			}
			else if(i%13 == 11){//Queen
				deck[i].val = 10;
				deck[i].face = "Q";
			}
			else if(i%13 == 12){//King
				deck[i].val = 10;
				deck[i].face = "K";
			}
			if(i < 13){
				deck[i].suit = "\u2660"; //Spade
			}
			else if(i >= 13 && i < 26){
				deck[i].suit = "\u2665"; //Heart
			}
			else if( i >=26 && i < 39){
				deck[i].suit = "\u2663"; //Club
			}
			else if(i >=39 && i < 52){
				deck[i].suit = "\u25c6"; //Diamond
			}
		}
		
		
	}
	public void shuffleDeck(){
		Card temp; int ran;
		for(int i = deck.length - 1; i > 0; i--){
			ran = rand.nextInt(i+1);
			temp = deck[ran];
			deck[ran] = deck[i];
			deck[i] = temp;
		}
	}
	public void printDeck(){
		textSize(50);
		String collection = "";
		for(int i = 0; i < deck.length; i++){
			collection += deck[i].face;
			collection += deck[i].suit;
			collection += " ";
		}
		text(collection, 0, 0, 800, 600);
	}
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "blackjack.BlackJack" });
	}
}
