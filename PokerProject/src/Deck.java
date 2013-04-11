import Tables.*;


public class Deck {
	private final int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };
	private final String[] value_str = {"",	"Straight Flush", "Four of a Kind",	"Full House", "Flush", "Straight",
		"Three of a Kind", "Two Pair", "One Pair", "High Card" };
	private final int[][] perm7 = { { 0, 1, 2, 3, 4 }, { 0, 1, 2, 3, 5 }, { 0, 1, 2, 3, 6 }, { 0, 1, 2, 4, 5 }, 
		{ 0, 1, 2, 4, 6 }, { 0, 1, 2, 5, 6 }, { 0, 1, 3, 4, 5 }, { 0, 1, 3, 4, 6 }, { 0, 1, 3, 5, 6 }, 
		{ 0, 1, 4, 5, 6 }, { 0, 2, 3, 4, 5 }, { 0, 2, 3, 4, 6 }, { 0, 2, 3, 5, 6 }, { 0, 2, 4, 5, 6 }, 
		{ 0, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5 }, { 1, 2, 3, 4, 6 }, { 1, 2, 3, 5, 6 }, { 1, 2, 4, 5, 6 }, 
		{ 1, 3, 4, 5, 6 }, { 2, 3, 4, 5, 6 } };
	
	public int[] deck;
	
	
	public Deck(int size) {
		deck = new int[size];
		initDeck();
		shuffleDeck();
	}
	
	/** Initialize Deck **/
	private void initDeck() {
		 int i, j, n = 0 , suit = 0x8000;
		 	
		 // Loop to shift suit bit
		 for (i = 0; i < 4; i++, suit >>= 1)
			 for (j = 0; j < 13; j++, n++)
				 deck[n] = primes[j] | (j << 8) | suit | (1 << (16+j));
	}
	
	public void shuffleDeck() {
	    int i, n, temp[] = new int[52];

	    for ( i = 0; i < 52; i++ )
	        temp[i] = deck[i];

	    for ( i = 0; i < 52; i++ )
	    {
	        do {
	            n = (int)(51.9999999 * Math.random());
	        } while ( temp[n] == 0 );
	        deck[i] = temp[n];
	        temp[n] = 0;
	    }
	}
	
	
	/** Evaluate Cards **/
	private short getHandValue(int c1, int c2, int c3, int c4, int c5) {
	    int q;
	    short s;

	    q = (c1|c2|c3|c4|c5) >> 16;

	    /* Check for Flushes and StraightFlushes
	    */
	    if ((c1 & c2 & c3 & c4 & c5 & 0xF000) != 0 )
	    	return( Flushes.flushes[q] );

	    /* Check for Straights and HighCard hands
	    */
	    s = Unique.unique5[q];
	    if (s != 0)
	    	return (s);

	    /* Let's do it the hard way
	    */
	    q = (c1&0xFF) * (c2&0xFF) * (c3&0xFF) * (c4&0xFF) * (c5&0xFF);
	    q = searchHand(q);

	    return(Values.values[q]);
	}

	public short evaluate5Cards(int[] hand) {
	    int c1, c2, c3, c4, c5;

	    c1 = hand[0];
	    c2 = hand[1];
	    c3 = hand[2];
	    c4 = hand[3];
	    c5 = hand[4];

	    return(getHandValue(c1,c2,c3,c4,c5));
	}
	
	public short evaluate7Cards(int[] hand ) {
	    int i, j, subhand[] = new int[5];
	    short q, best = 9999;
	    
		for ( i = 0; i < 21; i++ ) {
			
			for ( j = 0; j < 5; j++ )
				subhand[j] = hand[ perm7[i][j] ];
			q = this.evaluate5Cards(subhand);
			if (q < best)
				best = q;
		}
		return best;
	}
	
	private int searchHand(int key) {
	    int low = 0, high = 4887, mid;

	    while (low <= high) {
	        mid = (high+low) >> 1;      // divide by two
	        if (key < Products.products[mid])
	            high = mid - 1;
	        else if (key > Products.products[mid])
	            low = mid + 1;
	        else
	            return(mid);
	    }
	    System.out.println("ERROR:  no match found; key = " + key);
	    return(-1);
	}
	
	public int rankHand(int val) {
	    if (val > 6185) return(9);  // 1277 high card
	    if (val > 3325) return(8);  // 2860 one pair
	    if (val > 2467) return(7);  //  858 two pair
	    if (val > 1609) return(6);  //  858 three-kind
	    if (val > 1599) return(5);  //   10 straights
	    if (val > 322)  return(4);  // 1277 flushes
	    if (val > 166)  return(3);  //  156 full house
	    if (val > 10)   return(2);  //  156 four-kind
	    return(1);                  //   10 straight-flushes
	}
	
	public void getAllProababilities5() {
		int hand[] = new int[5], freq[] = new int[10];
	    int a, b, c, d, e, i, j;
	    
	    for(a=0;a<48;a++)
	    {
	    	hand[0] = deck[a];
	    	for(b=a+1;b<49;b++)
	    	{
			    hand[1] = deck[b];
			    for(c=b+1;c<50;c++)
			    {
			    	hand[2] = deck[c];
			    	for(d=c+1;d<51;d++)
			    	{
			    		hand[3] = deck[d];
			    		for(e=d+1;e<52;e++)
			    		{
			    			hand[4] = deck[e];
			    			
			    			//printHand(hand, 5);
			    			i = evaluate5Cards(hand);
			    			j = rankHand(i);
			    			//System.out.println(i);
			    			freq[j]++;
			    		}
			    	}
			    }
	    	}
	    }

	    for(i=1;i<=9;i++)
	    	System.out.println(value_str[i] + ":" + freq[i]);
	}
	
	public void getAllProababilities7() {
		int hand[] = new int[7], freq[] = new int[10];
	    int a, b, c, d, e, i, j, x, y;
	    
	    for(x=0;x<46;x++)
	    {
	    	hand[0] = deck[x];
	    	for(y=x+1;y<47;y++)
		    {
	    		hand[1] = deck[y];
	    		for(a=y+1;a<48;a++)
	    	    {
	    	    	hand[2] = deck[a];
	    	    	for(b=a+1;b<49;b++)
	    	    	{
	    			    hand[3] = deck[b];
	    			    for(c=b+1;c<50;c++)
	    			    {
	    			    	hand[4] = deck[c];
	    			    	for(d=c+1;d<51;d++)
	    			    	{
	    			    		hand[5] = deck[d];
	    			    		for(e=d+1;e<52;e++)
	    			    		{
	    			    			hand[6] = deck[e];
	    			    			
	    			    			//printHand(hand, 5);
	    			    			i = evaluate7Cards(hand);
	    			    			j = rankHand(i);
	    			    			//System.out.println(i);
	    			    			freq[j]++;
	    			    		}
	    			    	}
	    			    }
	    	    	}
	    	    }
		    }
	    }
	    
	    

	    for(i=1;i<=9;i++)
	    	System.out.println(value_str[i] + ":" + freq[i]);
	}
	
	public void printHand(int[] hand, int n) {
	    int i, r;
	    String suit;
	    final char[] rank = "23456789TJQKA".toCharArray();
	    
	    for (i = 0; i < n; i++) {
	        r = (hand[i] >> 8) & 0xF;
	        if ((hand[i] & 0x8000) != 0)
	            suit = "c";
	        else if ((hand[i] & 0x4000) != 0)
	            suit = "d";
	        else if ((hand[i] & 0x2000) != 0)
	            suit = "h";
	        else
	            suit = "s";
	        
	        System.out.print(rank[r] + suit + " ");
	    }
	    System.out.println();
	}
}
