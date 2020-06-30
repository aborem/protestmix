# Protest Mix for Android

To be eventually used as a way to bypass sms snooping.

## Next Steps

### Authentication
- How will users sign on? (My guess is local authentication with encrypted local db of user keys).

### Shuffling Protocol
- Will shuffling be based on convenience (whoever is closest) or deterministic/preplanned?
- Will shuffling be at specific intervals or whenever the opportunity arises (someone is close)?
- How many people should be shuffling at any given point?
- What if a person can't swap phones at the moment?
- How will we tell the app shuffling occured? Do we need other information during authentication besides new individual's key?
- Do we need protocol for returning phones at end? (I don't think so since I can't see how people will be )

### Message Forwarding Protocol
- How will nodes know who to send messages to? Who will keep track of what information?
- How will the receiver know who sent the original message?

### Encryption + Other failsaifs
- [ ] Clearing tables through abort button (?) and on app close/quit/startup (maybe we don't want to do this?)
- [ ] Encrypting all databases
- [ ] Maybe encrypting sms itself?

---

## In Progress and Completed

### Message Forwarding
- [ ] Creates conversation when new message is received who is not contact.
- [ ] Uses internal storage to determine who to forward message to. (Might change later when shuffling/forwarding protocols are developed)
- [x] Message forwarding based on indicator.

### UI Setup
- [ ] Display number of unread messages
- [ ] Sort contact cards by number of unread messages
- [x] Message bubbles (sent + receive).
- [x] Way of displaying contacts.
- [x] Way of starting conversations.
- [x] Way of accessing past conversations.

### Storage
- [ ] Finish integration of forwarding table to actually be queried when messages are received.
- [x] Message table
- [x] Forwarding table
- [x] Contacts table

---

## ISSUES
- [ ] Send button is blank
- [ ] Phone number formatting is not working properly
- [ ] More reliable way of storing contacts in messages table (maybe UUID?)
- [ ] Standardize phone number String formatting
