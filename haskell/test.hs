-- ghci
-- :set prompt ">"
-- :?


-- Simple expressions
2 + 15 -- 17
49 * 100 -- 4900
1882 - 1472 -- 420
50 * (100 - 4999) -- -244950
5 / 2 -- 2.5
5 * (-3) -- -15

-- boolean
True -- True
False -- False
True && False -- False
True && True -- True
False || True -- True
not False -- True
not (True && True) -- False

-- equal && not equal
5 == 5 -- True
1 == 0 -- False
5 /= 5 -- False
5 /= 4 -- True
"hi" == "hi" -- True

-- function call has high priority level
succ 9 + max 5 4 + 1 -- 16
(succ 9) + (max 5 4) + 1 -- 16
div 92 10 -- 9
-- infix
92 `div` 10 -- 9

succ 8 -- 9
min 9 10 -- 9
max 100 101 -- 101


-- Simple Function
doubleMe x = x + x
doubleUs x y = doubleMe x + doubleMe y
doubleSmallNumber x = (if x > 100 then x else x * 2) + 1

doubleUs 2 3 -- 10
doubleSmallNumber 2 -- 5
doubleSmallNumber 101 -- 102


-- List
let lostNumbers = [4,8,15,16,23,48]
lostNumbers -- [4,8,15,16,23,48]
-- concat
[1,2,3,4] ++ [9,10,11,12] -- [1,2,3,4,9,10,11,12]
-- char-string is list
"hello" ++ " " ++ "world" -- "hello world"
['h','e','l','l','o'] -- "hello"
['w','o'] ++ ['o','t'] -- "woot"
-- head insert
'A':" SMALL CAT" -- "A SMALL CAT"
5:[1,2,3,4,5] -- [5,1,2,3,4,5]
1:2:3:[] --[1,2,3]
-- get element by index
"Steve Buscemi" !! 6 -- 'B'
 [9.4,33.2,96.2,11.2,23.25] !! 1 -- 33.2
[[1,2,3,4],[5,3]] -- [[1,2,3,4],[5,3]]
[3,2,1] > [2,1,0] -- True

head [5,4,3,2,1] -- 5
tail [5,4,3,2,1] -- [4,3,2,1]
last [5,4,3,2,1] -- 1
init [5,4,3,2,1] -- [5,4,3,2]
length [5,4,3,2,1] -- 5

null [] -- True

reverse [5,4,3,2,1] -- [1,2,3,4,5]
take 3 [5,4,3,2,1] -- [5,4,3]
take 1 [3,9,3] -- [3]
take 5 [1,2] -- [1,2]
take 0 [6,6,6] -- []

drop 3 [8,4,2,1,5,6] -- [1,5,6]
drop 0 [1,2,3,4] -- [1,2,3,4]
drop 10 [1,2,3,4] -- []
minimum [8,4,2,1,5,6] -- 1
maximum [1,9,2,3,4] -- 9

sum [3,4,5] -- 12
product [6,2,1,2] -- 24

4 `elem` [3,4,5,6] -- True

-- Range
[1..5] -- [1,2,3,4,5]
['a'..'z'] -- "abcdefghijklmnopqrstuvwxyz"
['K'..'Z'] -- "KLMNOPQRSTUVWXYZ"
[2,4..10] -- [2,4,6,8,10]
[3,6..20] -- [3,6,9,12,15,18]
[5,4..1] -- [5,4,3,2,1]

take 10 (cycle [1,2,3]) -- [1,2,3,1,2,3,1,2,3,1]
take 12 (cycle "LOL ") -- "LOL LOL LOL "
take 3 (repeat 5) -- [5,5,5]
replicate 3 10 -- [10,10,10]

-- List Comprehension
[x*2 | x <- [1..10]] -- [2,4,6,8,10,12,14,16,18,20]
[x*2 | x <- [1..10], x*2 >= 12] -- [12,14,16,18,20]
[x | x <- [50..100], x `mod` 7 == 3] -- [52,59,66,73,80,87,94]
boomBangs xs = [ if x < 10 then "BOOM!" else "BANG!" | x <- xs, odd x]
boomBangs [7..13] -- ["BOOM!","BOOM!","BANG!","BANG!"]
[x | x <- [10..20], x /= 13, x /= 15, x /= 19] -- [10,11,12,14,16,17,18,20]
[x*y | x <- [2,5,10], y <- [8,10,11]] -- [16,20,22,40,50,55,80,100,110]
[x*y | x <-[2,5,10], y <- [8,10,11], x*y > 50] -- [55,80,100,110]

let nouns = ["hobo","frog"]
let adjectives = ["lazy","grouchy"]
[adjective ++ " " ++ noun | adjective <- adjectives, noun <- nouns] --["lazy hobo","lazy frog","lazy pope","grouchy hobo","grouchy frog","grouchy pope"]

length' xs = sum [1 | _ <- xs]
length' "hi" -- 2

removeNonUppercase :: String -> String
removeNonUppercase st = [c | c <- st, c `elem` ['A'..'Z']]
removeNonUppercase "IdontLIKEFROGS" -- "ILIKEFROGS"

let xxs = [[1,3,5,2,3,1,2,4,5],[1,2,3,4,5,6,7,8,9],[1,2,4,2,1,6,3,1,3,2,3,6]]
[ [ x | x <- xs, even x ] | xs <- xxs] -- [[2,2,4],[2,4,6,8],[2,4,2,6,2,6]]


-- Tuple
fst (8,11) -- 8
snd (8,11) -- 10
fst ("WoW", False) -- "WoW"

zip [1 .. 5] ["one", "two", "three", "four", "five"] -- [(1,"one"),(2,"two"),(3,"three"),(4,"four"),(5,"five")]
zip [1..] ["apple", "orange", "cherry", "mango"] -- [(1,"apple"),(2,"orange"),(3,"cherry"),(4,"mango")]

let triangles = [ (a,b,c) | c <- [1..10], b <- [1..10], a <- [1..10] ]


-- Types and Typeclasses
-- Types
:t 'a' -- 'a' :: Char
:t True -- True :: Bool
:t "HELLO!" -- "HELLO!" :: [Char]
:t (True, 'a') -- (True, 'a') -- :: (Bool, Char)
:t 4 == 5 -- 4 == 5 :: Bool
-- Int
addThree :: Int -> Int -> Int -> Int
addThree x y z = x + y + z
addThree 1 2 3 -- 6
-- Integer
factorial :: Integer -> Integer
factorial n = product [1..n]
factorial 50 -- 30414093201713378043612608166064768844377641568960512000000000000
-- Float
circumference :: Float -> Float --or Double -> Double
circumference r = 2 * pi * r

-- Type variables
:t head -- head :: [a] -> a
:t fst -- fst :: (a, b) -> a
:t (==) -- (==) :: Eq a => a -> a -> Bool
:t (>) -- (>) :: Ord a => a -> a -> Bool
"Abrakadabra" `compare` "Zebra" -- LT
5 `compare` 3 -- GT
-- Show
show 3 -- "3"
show True -- "True"
-- Read
read "True" :: Bool -- True
read "True" || False -- True
read "5" :: Int -- 5
read "5" :: Float -- 5.0
read "[1,2,3,4]" :: [Int] -- [1,2,3,4]
read "(3, 'a')" :: (Int, Char) -- (3, 'a')
-- Enum
[3..5] -- [3,4,5]
[LT .. GT] -- [LT,EQ,GT]
succ 'B' -- 'C'
-- Bounded
minBound :: Int -- -9223372036854775808
maxBound :: Char -- '\1114111'
maxBound :: Bool -- True
maxBound :: (Bool, Int, Char) -- (True,9223372036854775807,'\1114111')
-- Num
:t 20 -- 20 :: Num p => p
:t (*) -- (*) :: Num a => a -> a -> a
-- Integral: Int, Integer
-- Floating: Float, Double


-- Parttern matching
lucky :: (Integral a) => a -> String
lucky 7 = "LUCKY NUMBER SEVEN!"
lucky x = "Sorry, you're out of luck, pal!"

sayMe :: (Integral a) => a -> String
sayMe 1 = "One!"
sayMe 2 = "Two!"
sayMe 3 = "Three!"
sayMe x = "Not between 1 and 3"

factorial :: (Integral a) => a -> a
factorial 0 = 1
factorial n = n * factorial (n - 1)

charName :: Char -> String
charName 'a' = "Albert"
charName 'b' = "Broseph"
charName 'c' = "Cecil"