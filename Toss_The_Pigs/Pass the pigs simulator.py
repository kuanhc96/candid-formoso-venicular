import numpy as np
import random as rand
import time as t
class PassThePigsSimulator1P: # for single player

    def readTosses():
        dictTosses = {} # Dictionary: key is toss result, value is array -- first entry probability, second entry points
        file = open('Pass_the_pigs.txt')
        possibleEvents = file.readlines()
        for line in possibleEvents:
            event = line.split()
            dictTosses[event[0]] = [float(event[1]),int(event[2])] # Dictionary: key is toss result, value is array -- first entry probability, second entry points
        return dictTosses
    
    DICT = readTosses()
    
    def __init__(self, player, bankPoints, myTurn, turnPoints = 0, numRolls = 0):
        # default values, serves like two constructors in Java
        # turn points are the points that a player has accumulated over the turn before banking
        # numRolls are the number of rolls made in any given turn
        self.bankPoints = bankPoints
        self.turnPoints = turnPoints
        self.numRolls = numRolls
        self.player = player
        self.myTurn = myTurn
    
    def displayPlayer(self):
        print(self.player)
        
    def displayPoints(self):
        print(self.bankPoints)
    
    def displayTurn(self):
        print(self.myTurn)
        
    def isWinner(self):
        if self.bankPoints >= 100:
            return True
        else:
            return False
    
    def singularRollResult(self, toss): # used in pointCalc()
        keys = list(self.DICT.keys())
        if toss < self.DICT[keys[0]][0]: # accessing the first element of the array corresponding to the first key -- pink
            # pink
            return keys[0]
        elif toss < self.DICT[keys[1]][0]:
            # dot
            return keys[1]
        elif toss < self.DICT[keys[2]][0]:
            # razorback
            return keys[2]
        elif toss < self.DICT[keys[3]][0]:
            # trotter
            return keys[3]
        elif toss < self.DICT[keys[4]][0]:
            # snouter
            return keys[4]
        else:
            # leaning jowler
            return keys[5]        

    def pointCalc(self): # used in updateBankPoints()
        toss1 = rand.randint(0,100)/100
        toss2 = rand.randint(0,100)/100
        result1 = self.singularRollResult(toss1)
        result2 = self.singularRollResult(toss2)
        self.numRolls = self.numRolls + 1
        
        
        if (result1 == 'dot' and result2 == 'pink') or (result2 == 'dot' and result1 == 'pink'):
            print('Pig Out! You lost all points in this turn!')
            return 0
        elif result1 == result2: # if same roll result, bonus points awarded
            if result1 == 'pink' or result1 == 'dot': # double pink or double dot = 1
                print('2x sides! +1 point!')
                return 1
            elif result1 == 'razorback':
                print('2x razorback! +20 points!')
                return 20
            elif result1 == 'trotter': # double razorback or double trotter = 20
                print('2x trotter! +20 points!')
                return 20
            elif result1 == 'snouter': # double snouter = 40
                print('2x snouter! +40 points!')
                return 40
            else:
                print('2x leaning jowler!? You deserve a Nobel Prize! +60 points!')
                return 60 # double leaning jowler = 60
        else:
            print('1 ' + result1 + ' and 1 ' + result2 + ': +'
                  + str(self.DICT[result1][1] + self.DICT[result2][1]) + ' points!')
            return self.DICT[result1][1] + self.DICT[result2][1]
        # return the sum of the second element of the value, which are the total points
        #e.g pink + trotter = 5
        
    def updateBankPoints(self, other):         
        # passThePigs = False
        # should update with self.myTurn
        if other.myTurn == False:
            self.myTurn = True
        while(self.myTurn):
            print('Player Rolls!')
            t.sleep(1)
            print('...')
            t.sleep(1)
            currentPoints = self.pointCalc()
            self.turnPoints = self.turnPoints + currentPoints
            if currentPoints == 0: # rolled one dot and one pink
                print('You wasted ' + str(self.turnPoints) +' points!')
                print('You rolled ' + str(self.numRolls) + ' number of times')
                self.myTurn = False
                self.turnPoints = 0
                self.numRolls = 0
                t.sleep(2)
            else:
                print('Continue Rolling? (y/n)')
                print('(You currently have ' + str(self.turnPoints) +' point(s)')
                print('and ' + str(self.bankPoints) + ' in the bank')
                if self.numRolls == 1:
                    print('That was your ' + str(self.numRolls) + 'st roll)')
                elif self.numRolls == 2:
                    print('That was your ' + str(self.numRolls) + 'nd roll)')
                elif self.numRolls == 3:
                    print('That was your ' + str(self.numRolls) + 'rd roll)')
                else:
                    print('That was your ' + str(self.numRolls) + 'th roll)')
                
                userResponse = input().lower()
                if userResponse[0] == 'y': # yes, continue rolling
                    self.myTurn = True
                else:
                    self.myTurn = False # no, stop rolling
                #self.turnPoints = self.turnPoints + currentPoints
        self.bankPoints = self.bankPoints + self.turnPoints # banking the turnPoints
        self.turnPoints = 0 # resetting turn points
        self.numRolls = 0 # resetting numRolls
        
    
    def greaterBankPointsTo(self, other):
        if not isinstance(other, PassThePigsSimulator1P):
            return NotImplemented
        return self.bankPoints > other.bankPoints
    
    def bankPointsDifference(self, other): # if < 0, self has less points than other
        if not isinstance(other, PassThePigsSimulator1P):
            return NotImplemented
        return self.bankPoints - other.bankPoints
    
    def greaterTurnPointsTo(self, other):
        if not isinstance(other, PassThePigsSimulator1P):
            return NotImplemented
        return self.turnPoints > other.turnPoints
    
    def turnPointsDifference(self, other): # if < 0, self has less points than other
        if not isinstance(other, PassThePigsSimulator1P):
            return NotImplemented
        return self.turnPoints - other.turnPoints
    
class PassThePigsSimulator2P(PassThePigsSimulator1P):
        
    # attempting to create AI with strategy found online,
    # credits to http://passpigs.tripod.com/strat.html
    def aiRollingStrategy(self, opponentScore, myScore, myTurnScore, round):
        # answers the question of, Continue Rolling? in the
        # updateBankPoints()
        if myTurnScore + myScore >= 100:
            return False
        elif opponentScore >= 85 or myScore >= 90:
            # if opponentScore is greater than or equal to 85 or
            # myScore is greater than or equal to 90, roll
            return True
        elif round == 1 and myTurnScore >= 15:
            # if after one roll myTurnScore is greater than or equal to 90
            # then don't roll 
            return False
        #elif myScore > opponentScore - 20:
            # if myScore is within 20 points of opponentScore
            # then roll
        #    return True
        elif myTurnScore > 20:
            # if myTurnScore is greater than 20, then don't roll
            return False
        else:
            # in every other case, roll
            return True
        
    def aiUpdateBankPoints(self, other):         
        if other.myTurn == False:
            self.myTurn = True
        print('AI Rolls!')
        t.sleep(1)
        print('...')
        t.sleep(1) 
        while(self.myTurn):
            currentPoints = self.pointCalc()
            self.turnPoints = self.turnPoints + currentPoints
            if currentPoints == 0: # rolled one dot and one pink
                print('The AI wasted ' + str(self.turnPoints) +' points!')
                self.myTurn = False
                self.turnPoints = 0
                self.numRolls = 0
                t.sleep(2)
            else:
                t.sleep(1)
                print('Continue Rolling? (y/n)')                
                print('(AI currently has ' + str(self.turnPoints) +' point(s)')
                print('and ' + str(self.bankPoints) + ' in the bank')
                if self.numRolls == 1:
                    print('That was the AI\'s ' + str(self.numRolls) + 'st roll)')
                elif self.numRolls == 2:
                    print('That was the AI\'s ' + str(self.numRolls) + 'nd roll)')
                elif self.numRolls == 3:
                    print('That was the AI\'s ' + str(self.numRolls) + 'rd roll)')
                else:
                    print('That was the AI\'s ' + str(self.numRolls) + 'th roll)')
                t.sleep(2)
                aiResponse = self.aiRollingStrategy(other.bankPoints, self.bankPoints, self.turnPoints, self.numRolls)
                if aiResponse: # yes, continue rolling
                    print('yes.')
                    self.myTurn = True
                else:
                    print('no.')
                    self.myTurn = False # no, stop rolling
                t.sleep(1)
                #self.turnPoints = self.turnPoints + currentPoints
        self.bankPoints = self.bankPoints + self.turnPoints # banking the turnPoints
        self.turnPoints = 0 # resetting turn points
        self.numRolls = 0 # resetting numRolls

def determineOrder(playersArray):# does not work yet
    firstPlayer = playersArray[rand.randint(0, len(playersArray)-1)] # randomly choose who goes first
    
def main():
    print('Welcome to a game of Toss The Pigs! Current functionalities only allow you to play against an AI...')
    t.sleep(5)
    #print('(Enter a number from 1 - 3): ') # need to add fool-proof method
    #numPlayers = int(input())
    #players = []
    #for i in range(numPlayers):
    #    print('Enter name of player ' + str(i))
    #    players.append()
    #print('Would you like to play against a computer? (y/n)') # need to add fool-proof method
    #print('(Adding a computer player increases the total players by 1)')
    #computerOrNot = input().lower()
    
    player1 = PassThePigsSimulator1P('Kuan',0 ,True)
    aiPlayer = PassThePigsSimulator2P('AI',0 , False)
    while(player1.isWinner() == False and aiPlayer.isWinner() == False): # no winner
        
        player1.updateBankPoints(aiPlayer)
        
        aiPlayer.aiUpdateBankPoints(player1)
    if player1.isWinner():
        print('The Winner is ' + player1.player)
    else:
        print('The AI Won!')
    

main()