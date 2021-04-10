function refineText(source, options) {
  return [
    normalizeWhiteSpaces,
    compactWhiteSpaces,
    maskBannedWords,
    trimWhitespaces,
  ].reduce((value, filter) => filter(value, options), source);
}

function trimWhitespaces(value) {
  return value.trim();
}

function normalizeWhiteSpaces(value) {
  return value.replace("\t", " ");
}

function compactWhiteSpaces(value) {
  return value.indexOf("  ") < 0
    ? value
    : compactWhiteSpaces(value.replace("  ", " "));
}

function maskBannedWords(value, options) {
  return options ? options.bannedWords.reduce(maskBannedWord, value) : value;
}

function maskBannedWord(value, bannedWord) {
  return value.replace(bannedWord, "*".repeat(bannedWord.length));
}

module.exports = refineText;
