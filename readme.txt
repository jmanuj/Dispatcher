approach 1

use a hashmap for predicates and events
predicate :

(c1 = "in") && (c2 = "news.php" || c2 = "home.php") && (c3 = "nytimes.com")

hashmap representation

c1 => [in]
c2 => [news.php, home.php]
c3 => [nytimes.com]

event representation (5 fields)

c1 => in
c2 => home.php
c3 => wsj.com
c4 => wikipedia

result : no match

match in the predicate hashmap and find in the list
