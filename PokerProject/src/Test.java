
public class Test {
	
	public static void main(String[] args) {
		/*** xxxAKQJT 98765432 CDHSrrrr xxPPPPPP ***/
		int suitMask = 0b00000000_00000000_11110000_00000000;
		suitMask = 0xF000;
        
		Deck deck = new Deck(52);
		for (int i = 0; i < deck.deck.length; i++)
			System.out.println(Integer.toBinaryString(deck.deck[i]));
		
		int c1 = deck.deck[7], c2 = deck.deck[19], c3 = deck.deck[23], c4 = deck.deck[44], c5 = deck.deck[15];
		
		if ((c1 & c2 & c3 & c4 & c5 & suitMask) != 0) {
			System.out.println("Flush!");
			//int q = (c1 | c2 | c3 | c4 | c5) >> 16;
			//System.out.println(q);
		}
		
		int q = (c1 | c2 | c3 | c4 | c5) >> 16;
		System.out.println(Integer.toBinaryString(q));
		
		deck.getAllProababilities5();
		//int[] hand = deck.deck;
		//int i = deck.evaluate7Cards(hand);
		//deck.printHand(hand, 7);
		//System.out.println(deck.rankHand(i));
	}

}
