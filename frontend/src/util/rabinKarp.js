export function rabinKarpHash(str, b = 31, m = 1e9 + 9) {
  let hash = 0,
    power = 1;
  for (let i = 0; i < str.length; i++) {
    hash = (hash + (str[i].charCodeAt() - "a".charCodeAt() + 1) * power) % m;
    power = (power * b) % m;
  }
  if (hash <= 0) {
    hash = (hash + m) % m;
    if (hash === 0) {
      hash = 1;
    }
  }
  return hash;
}
