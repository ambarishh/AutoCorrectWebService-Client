AutoCorrect is a local Web-service that:
1. Accepts a text string from a client.
2. Sends it to the Spell Checker web service
(http://wsf.cdyne.com/SpellChecker/check.asmx?wsdl) to find all spelling errors.
3. Replaces all errors with their first suggested correct word.
4. Returns the corrected string back to the client.
