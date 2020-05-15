package schmille.mcbookhelper;

import schmille.mcbookhelper.data.enums.EnumGameEdition;
import schmille.mcbookhelper.data.enums.EnumMethod;
import schmille.mcbookhelper.exceptions.WillNotFitException;

public class Main {

    public static void main(String[] args) throws WillNotFitException {
        Control c = new Control();

        c.autopage("I                                                                                      was walking down the street when out the corner of my eye\n" +
                "I saw a pretty little thing approaching me\n" +
                "She said, \"I never seen a man, who looks so all alone\n" +
                "And could you use a little company?\n" +
                "If you can pay the right price, your evening will be nice\n" +
                "But you can go and send me on my way\"\n" +
                "I said, \"You're such a sweet young thing, why you do this to yourself?\"\n" +
                "She looked at me and this is what she said\n" +
                "Oh there ain't no rest for the wicked\n" +
                "Money don't grow on trees\n" +
                "I got bills to pay, I got mouths to feed\n" +
                "There ain't nothing in this world for free\n" +
                "Oh no, I can't slow down, I can't hold back\n" +
                "Though you know, I wish I could\n" +
                "Oh no there ain't no rest for the wicked\n" +
                "Until we close our eyes for good\n" +
                "Not even fifteen minutes later after walking down the street\n" +
                "When I saw the shadow of a man creep out out of sight\n" +
                "And then he swept up from behind, he put a gun up to my head\n" +
                "He made it clear he wasn't looking for a fight\n" +
                "He said, \"Give me all you've got, I want your money not your life\n" +
                "But if you try to make a move I won't think twice\"\n" +
                "I told him, \"You can have my cash, but first you know I gotta ask\n" +
                "What made you want to live this kind of life?\"\n" +
                "He said\n" +
                "Oh there ain't no rest for the wicked\n" +
                "Money don't grow on trees\n" +
                "I got bills to pay, I got mouths to feed\n" +
                "There ain't nothing in this world for free\n" +
                "Oh no, I can't slow down, I can't hold back\n" +
                "Though you know, I wish I could\n" +
                "Oh no there ain't no rest for the wicked\n" +
                "Until we close our eyes for good\n" +
                "Well now a couple hours past and I was sitting in my house\n" +
                "The day was winding down and coming to an end\n" +
                "And so I turned on the TV and flipped it over to the news\n" +
                "And what I saw I almost couldn't comprehend\n" +
                "I saw a preacher man in cuffs, he'd taken money from the church\n" +
                "He'd stuffed his bank account with righteous dollar bills\n" +
                "But even still I can't say much because I know we're all the same\n" +
                "Oh yes we all seek out to satisfy those thrills\n" +
                "Oh there ain't no rest for the wicked\n" +
                "Money don't grow on trees\n" +
                "We got bills to pay, we got mouths to feed\n" +
                "There ain't nothing in this world for free\n" +
                "Oh no we can't slow down, we can't hold back\n" +
                "Though you know we wish we could\n" +
                "Oh no there ain't no rest for the wicked\n" +
                "Until we close our eyes for good", EnumMethod.FANCY, EnumGameEdition.JAVA);

        for(String s : c.getPages()) {
            System.out.println(s + "\n");
        }
    }
}
