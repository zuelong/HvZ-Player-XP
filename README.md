# HvZ-Player-XP
A Java application to track player experience using magnetic stripe cards.

## Card Writer
  For the project, I used a cheap magnetic card writer found here: 
    
  http://www.amazon.com/MSR605-Magnetic-Card-Reader-Encoder/dp/B00DE8248Q/ref=sr_1_1?ie=UTF8&qid=1450653413&sr=8-1&keywords=magnetic+stripe+reader+writer

  This particular writer came with a piece of software that would let me load all of the card data in at once and write it to the cards one by one.

## Card Reader
  For reading the cards, I used a cheap magnetic card reader found here:
  
  http://www.amazon.com/Newest-Tracks-Magnetic-Stripe-Credit/dp/B00D3D3L8Y/ref=sr_1_1?ie=UTF8&qid=1450653792&sr=8-1&keywords=magnetic+stripe+reader
  
  These card readers are pretty much plug and play.  They act basically act as an extra keyboard for your computer.  To test it, open a text program (such as notepad), plug your card reader in, and swipe a card.

## Left Column
The left column is a list of all the players I have added to a local database.  I scraped all of their names and ID numbers through a python script I wrote beforehand.  The scaping feature will soon be added to this application.

## Right Column
The right column is for tracking mini-missions created by the moderators.  These are also stored in a local database.

