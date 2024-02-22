/*
Read file input, example input, first number = cost, second number = time: 
Store this file input in adjacency list using vectors
7
Austin|Detroit|350|250
Austin|Billings|400|300
Billings|El Paso|450|350
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class BestFlight {

    // each vertex has a single path to the destination vertex object called Flight
    // this is used in conjuction with a list of all vertices. Within each entry of
    // this list
    // is a list of all vertices that are connected to the vertex in the entry,
    // which is collection of these Flight these objects.
    static class Flight {
        String destination;
        int cost;
        int time;

        public Flight(String destination, int cost, int time) {
            this.destination = destination;
            this.cost = cost;
            this.time = time;
        }
    }

    // I could ignore this class and just have a list of strings, but I want to
    // follow an object oriented approach
    static class path {
        ArrayList<String> path;
        int cost = 0;
        int time = 0;

        public path(ArrayList<String> copyPath, int copyCost, int copyTime) {
            this.path = copyPath;
            this.cost = copyCost;
            this.time = copyTime;
        }

        public path(ArrayList<String> copyPath) {
            this.path = new ArrayList<String>();
            for(int i = 0; i < copyPath.size(); i++) {
                this.path.add(copyPath.get(i));
            }
        }
        
        //getter functions, used to get start city and destination city, path, and cost
        public String getStartCity() {
            return this.path.get(0);
        }

        public String getDestinationCity() {
            return this.path.get(this.path.size() - 1);
        }

        public ArrayList<String> getPath() {
            return this.path;
        }

        public void displayPath() {
            for (int i = 0; i < this.path.size(); i++) {
                System.out.print(this.path.get(i));
                if (i != this.path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }

        public int getCost() {
            return this.cost;
        }
        public int getTime() {
            return this.time;
        }

        public void addCost(int cost) {
            this.cost += cost;
        }

        public void addTime(int time) {
            this.time += time;
        }

        
    }

    public static void main(String[] args) throws IOException {
        String startCity = "";
        String destinationCity = "";
        String costOrTime = "";
        // This algorithm will find the best flight from one city to another
        // The best flight is the one with the lowest cost or shortest time, depending
        // on the user's choice
        // In a requested flight plan input file user will input the starting city,
        // destination city, and whether they want the lowest cost or shortest time
        // The algorithm will then find the best flight and print it out

        // create an empty adjacency list for the flight info using vectors.
        // The key is the city name and the value is a vector of Flight objects
        HashMap<String, ArrayList<Flight>> flightInfo = new HashMap<>();

        // read the input file and populate the adjacency list
        File inputFile = new File("FlightList.txt");
        Scanner scanner = new Scanner(inputFile);
        int numFlights = scanner.nextInt(); // read the number of flights
        scanner.nextLine(); // skip the rest of the first line
        for (int i = 0; i < numFlights; i++) {
            String[] flightInfoLine = scanner.nextLine().split("\\|");
            // display
            System.out.println(
                    flightInfoLine[0] + " " + flightInfoLine[1] + " " + flightInfoLine[2] + " " + flightInfoLine[3]);
            String city = flightInfoLine[0];
            String destination = flightInfoLine[1];
            int cost = Integer.parseInt(flightInfoLine[2]);
            int time = Integer.parseInt(flightInfoLine[3]);

            // add the flight to the adjacency list, flights are bidirectional
            // meaning that if there is a flight from Austin to Detroit, there is also a
            // flight from Detroit to Austin
            if (!flightInfo.containsKey(city)) {
                flightInfo.put(city, new ArrayList<>());
            }
            flightInfo.get(city).add(new Flight(destination, cost, time));
            if (!flightInfo.containsKey(destination)) {
                flightInfo.put(destination, new ArrayList<>());
            }
            flightInfo.get(destination).add(new Flight(city, cost, time));
        }
        scanner.close();
        // display the adjacency list
        System.out.println("Adjacency list:");
        for (String city : flightInfo.keySet()) {
            System.out.print(city + ": ");
            for (Flight flight : flightInfo.get(city)) {
                // display the destitions and the cost in time and money
                System.out.print(flight.destination + " " + "$" + flight.cost + " " + flight.time + ", ");
            }
            System.out.println();
        }

        // now we read the imput file, FlightPlans.txt, the trailing character is either
        // T or C, which means time or cost:
        // we will use this trailing character to determine whether we want to find the
        // shortest time or lowest cost
        /*
         * 2
         * Dallas|Houston|T
         * Chicago|Dallas|C
         */

        // create empty array of successfullPaths that reach the destination city
        ArrayList<path> successfullPaths = new ArrayList<path>();
        // create a path object, this will be used to store the current path. It will be
        // updated as we explore the paths
        // and when it reaches the destination city, it will be copied to the
        // successfullPaths array
        ArrayList<String> path = new ArrayList<String>();

        File flightPlansFile = new File("FlightPlans.txt");
        Scanner flightPlansScanner = new Scanner(flightPlansFile);
        int numFlightPlans = flightPlansScanner.nextInt(); // read the number of flight plans
        flightPlansScanner.nextLine(); // skip the rest of the first line
        for (int i = 0; i < numFlightPlans; i++) {
            String[] flightPlan = flightPlansScanner.nextLine().split("\\|");
            startCity = flightPlan[0];
            destinationCity = flightPlan[1];
            costOrTime = flightPlan[2];
            System.out.println("Flight plan: " + startCity + " to " + destinationCity + " " + costOrTime);

            // now we create a stack to store the cities to be visited, we can have
            // repetition of cities in the stack
            Stack<String> citiesToBeVisited = new Stack<String>();
            // add the start city to the stack
            citiesToBeVisited.push(startCity);
            // add the start city to the current path
            //path.add(startCity);
            // while the stack is not empty
            int countPaths = 0;
            while (!citiesToBeVisited.isEmpty()) {
                // display the stack for testing purposes
                System.out.println("\nStack: " + citiesToBeVisited);
                // pop the top of the stack
                String currentCity = citiesToBeVisited.pop();
                //add the current city to the current path
                path.add(currentCity);
                //display the city that was popped and added to the current path
                System.out.println("removed from stack: " + currentCity);
                path tempPath = new path(path);
                System.out.print("Current path: ");
                tempPath.displayPath();

                String cityToRemove = "";
                // if the current city is the destination city, and the path is not already in the successfullPaths array
                if (currentCity.equals(destinationCity)) {
                    System.out.println("Current city: " + currentCity + " is the destination city: " + destinationCity);
                    countPaths++;
                    //create a temporary path object to store the current path
                    tempPath = new path(path);
                    //display path for testing purposes
                    System.out.print("Successful path " + ": ");
                    tempPath.displayPath();
                    //remove tail cities from the path until we find a path that is not already in the successfullPaths array
                    //if the path is already in the successfullPaths array, then we need to remove the tail cities from the path
                    //until we find a path that is not already in the successfullPaths array
                    successfullPaths.add(new path(path));
                    //while(path.size() != 0 && tempPath) {
                     //   path.remove(path.size() - 1);
                    //}
                    //once the path is no longer a previous path, we continue to search for more paths
                    

                    //now we shorten the path until we find a city that goes to a city that is gonna be popped from stack next using peek. 
                    //once we find a city that goes to a city that is gonna be popped from stack next using peek, we stop shortening the path and 
                    //check if this path, starting from the starting city, is already in the successfullPaths array
                    //if the path is already in the successfullPaths array, then we need to remove the tail cities from the path
                    

                    //remove destination city from the path
                    System.out.println("Removing " + path.get(path.size() - 1) + " from path");
                    path.remove(path.size() - 1);
                    //display the path
                    path displayPath = new path(path);
                    System.out.print("Path after removing destination city: ");
                    displayPath.displayPath();


                    while(path.size() != 0) {
                        if(citiesToBeVisited.isEmpty()) {
                            System.out.println("Stack is empty, no more paths to check");
                            System.out.println("found " + countPaths + " paths");
                            break;
                        }
                        //if the city in the path is a city that goes to a city that is gonna be popped from stack next using peek
                        System.out.println("Checking if " + path.get(path.size() - 1) + " goes to " + citiesToBeVisited.peek());
                        boolean cityGoesToCityToBePoppedFromStackNext = false;
                        for(Flight flight : flightInfo.get(path.get(path.size() - 1))) {
                            if(flight.destination.equals(citiesToBeVisited.peek()) && !path.contains(flight.destination)) {
                                System.out.println(path.get(path.size() - 1) + " goes to " + citiesToBeVisited.peek());
                                cityGoesToCityToBePoppedFromStackNext = true;
                                break;//break out of for loop
                            }
                        }
                        if(cityGoesToCityToBePoppedFromStackNext) {
                            System.out.println("Now we check if the path up to this point is already in the successfullPaths array");
                            System.out.println("Path up to this point: ");
                            displayPath = new path(path);
                            displayPath.displayPath();
                            //we found a potential path that is not already in the successfullPaths array
                            //but first we must check if the path up to this point is already in the successfullPaths array
                            //if the path is already in the successfullPaths array, then we need to remove the tail cities from the path and keep searching
                            path tempPath2= new path(path);
                            //add the next city to be popped from the stack to the temp path

                            tempPath2.path.add(citiesToBeVisited.peek());
                            System.out.println("Temp path: ");
                            tempPath2.displayPath();

                            //if successful paths contains the temp path, then we need to keep searching for next path candidate
                            //search through each successful path entry, for each entry check if the path is equal to the temp path

                            boolean pathIsAlreadyInSuccessfullPathsArray = false;
                            System.out.println("Checking if the temp path is already in the successfullPaths array");
                            System.out.println("Temp path: ");
                            for(int j = 0; j < successfullPaths.size(); j++) {
                                //if the path is equal to the temp path
                                //iterate through array list that represents path and check if each city is equal
                                int k = 0;
                                System.out.println("Successful path " + (j + 1) + ": ");
                                successfullPaths.get(j).displayPath();

                                for(k = 0; k < tempPath2.getPath().size(); k++) {
                                    //if the city is not equal
                                    System.out.println("Comparing " + successfullPaths.get(j).getPath().get(k) + " to " + tempPath2.getPath().get(k));
                                    if(!successfullPaths.get(j).getPath().get(k).equals(tempPath2.getPath().get(k))) {
                                        //the path is not equal to the temp path
                                        break;
                                    }
                                    //else if equal keep counting
                                    
                                }
                                System.out.println("k = " + k);
                                //k is the last index that was checked of the temp path
                                System.out.println("tempPath2.getPath().size() = " + tempPath2.getPath().size());
                                if(k == (tempPath2.getPath().size())) {
                                    System.out.println("The path is already in the successfullPaths array");
                                    pathIsAlreadyInSuccessfullPathsArray = true;
                                    //break out of the outer while loop
                                    break;

                                }
                                else
                                {
                                    System.out.println("The path is not already in the successfullPaths array");
                                    continue;

                                }
                            

                            }
                            //if the path is not already in the successfullPaths array
                            System.out.printf("Now checking pathisalreadyinsuccessfullpathsarray flag: %b\n", pathIsAlreadyInSuccessfullPathsArray);
                            if(!pathIsAlreadyInSuccessfullPathsArray) {
                                //we found a path that is not already in the successfullPaths array
                                //we can stop shortening the path
                                System.out.println("Found a path that is not already in the successfullPaths array");
                                //print out the path for testing purposes
                                System.out.print("The path is" + ": ");
                                tempPath2.displayPath();
                                System.out.println("The real path is still: " + ": ");
                                displayPath = new path(path);
                                displayPath.displayPath();

                                break;
                            }
                            else {
                                //the path is already in the successfullPaths array
                                //we need to keep shortening the path
                                System.out.println("The path is already in the successfullPaths array");
                                //remove the city from the path
                                String cityRemoved = path.get(path.size() - 1);
                                System.out.println("Removing " + cityRemoved + " from path");
                                path.remove(path.size() - 1);
                                //display the path
                                displayPath = new path(path);
                                System.out.print("Path after removingg " + cityRemoved + ": ");
                                displayPath.displayPath();
                                //terminate entire program
                                //System.exit(0);
                                if(path.size() == 0) {
                                    System.out.println("Path is empty, no more paths to check");
                                    System.exit(0);
                                }
                            }
                        }
                        else {
                            System.out.println("The city in the path does not go to a city that is gonna be popped from stack next using peek");
                        //if the city in the path is not a city that goes to a city that is gonna be popped from stack next using peek
                        
                            //remove the city from the path
                            String cityRemoved = path.get(path.size() - 1);
                            System.out.println("Removing " + cityRemoved + " from path");
                            path.remove(path.size() - 1);
                            //display the path
                            displayPath = new path(path);
                            System.out.print("Path after removingg " + cityRemoved + ": ");
                            displayPath.displayPath();
                            //terminate entire program
                            //System.exit(0);
                            if(path.size() == 0) {
                                System.out.println("Path is empty, no more paths to check");
                                System.exit(0);
                            }
                        }
                        
                    }
                    
                }
                // else if the current city is not the destination city, keep going deeper into
                // the graph
                else {
                    int countOutgoingFlights = 0;
                    // for each city in the adjacency list of the current city
                    System.out.print("Outgoing notfromprevpath flights from " + currentCity + " : ");
                    for (Flight flight : flightInfo.get(currentCity)) {
                        // if the city is not already in the current path, prevent cycles
                        if (!path.contains(flight.destination)) {
                            // add the city to the stack
                            citiesToBeVisited.push(flight.destination);
                            //print the city that is being added
        //                    System.out.print(flight.destination + " ");
                            // don't need to worry about marking as visited, because we using for loop, so
                            // we don't visit the same city twice in a single iteration
              //              countOutgoingFlights++;
                        }
                        
                    }
                    //display the number of outgoing flights for testing purposes
                    //and display the outgoing flights for a city that was just popped from the stack
                    //System.out.print("Number of outgoing flights: " + countOutgoingFlights + " ");
                    //System.out.print("Outgoing flights from " + currentCity + " : ");

    //                System.out.println();
     //               if(countOutgoingFlights == 0) {
      //                  System.out.println("No outgoing flights from " + currentCity + " , removing from path");
       //                 path.remove(path.size() - 1);
     //               }
                }
            }
            System.out.println("Found " + countPaths + " paths");

            /*
             * Now find the shortest path from the start city to the destination city,
             * output to a file called FlightPlansOutput.txt
             * In more detail:
             * To solve this problem, you’ll need to implement an exhaustive search of all
             * flights.
             * To achieve this, you’ll implement an iterative backtracking algorithm (using
             * a stack).
             * As you are calculating the flight path, you will use the stack to “remember”
             * where you are in the search.
             * The stack will also be used in the event that you've gone down a path that
             * does not lead to the destination city.
             * In this case, you will “backtrack” to the last city you visited and continue
             * the search from there.
             * You will continue this process until you find the destination city or until
             * you have exhausted all possible paths.
             * If you find the destination city, you will output the flight path to a file
             * called FlightPlansOutput.txt.
             * If you exhaust all possible paths, you will output “No flight plan found” to
             * the file.
             * The output file will contain the flight path in the following format:
             * 
             * Flight 1: Dallas, Houston (Time)
             * Path 1: Dallas -> Houston. Time: 51 Cost: 101.00
             * Path 2: Dallas -> Austin -> Houston. Time: 86 Cost: 193.00
             * 
             * Flight 2: Chicago, Dallas (Cost)
             * Path 1: Chicago -> Austin -> Dallas. Time: 237 Cost: 242.00
             * Path 2: Chicago -> Austin -> Houston -> Dallas. Time: 282 Cost: 340.00
             */
            // exhaust all possible paths, keeping track of either cost or time, once all
            // paths have been exhausted, output the best path to a file called
            // FlightPlansOutput.txt
            // if the destination city is not found, output “No flight plan found” to the
            // file.

            // solution
            // keep track of the cost or time
            // then while exploring the paths, keep track of the cost or time of each path
            // keep track of the best path, if a path is found that is better than the
            // current best path, replace the best path with the new path
            // once all paths have been explored, output the best path to a file called
            // FlightPlansOutput.txt
            // if the destination city is not found, output “No flight plan found” to the
            // file.

            // create a variable to keep track of the cost or time
            // int recurringCostOrTime = 0;
            // create a list to store the current best path
            // after each path is explored, compare the cost or time of the current path to
            // the cost or time of the best path
            // if the current path is better than the best path, replace the best path with
            // the current path
            // once all paths have been explored, output the best three paths to a file
            // called FlightPlansOutput.txt
            // if the destination city is not found, output “No flight plan found” to the
            // file.

            // create a list to store the current path, along with the cost or time of the
            // current path at front of the list
            // ArrayList<String> currentPath = new ArrayList<String>();

            // now we begin exhausting all possible paths using breadth first search
            // we know we've exhausted all possible paths once stack is empty

        }

        //now we have all the paths in the successfullPaths array
        //now we sort the successfullPaths array by cost or time
        //if the user wants the lowest cost, we sort by cost
        //if the user wants the shortest time, we sort by time
        //run a sorting algorithm on the successfullPaths array
        flightPlansScanner.close();

        // now we output all paths to a file called FlightPlansOutput.txt
        // if the destination city is not found, output “No flight plan found” to the
        // file.
        // create a file called FlightPlansOutput.txt
        File flightPlansOutputFile = new File("FlightPlansOutput.txt");
        // create a file writer
        FileWriter flightPlansOutputFileWriter = new FileWriter(flightPlansOutputFile);
        // if the destination city is not found, output “No flight plan found” to the
        // file.
        if (successfullPaths.isEmpty()) {
            flightPlansOutputFileWriter.write("No flight plan found");
        } else {
            // iterate through each path entry in the successfullPaths array, count cost or
            // time of each path
            /*
             * Flight 1: Dallas, Houston (Time)
             * Path 1: Dallas -> Houston. Time: 51 Cost: 101.00
             * Path 2: Dallas -> Austin -> Houston. Time: 86 Cost: 193.00
             */
            // iterate through each path entry in the successfullPaths array, count cost or
            // time of each path

            //display purely for testing purposes just each path 
            for (int i = 0; i < successfullPaths.size(); i++) {
             System.out.println("Path " + (i + 1) + ": ");
               successfullPaths.get(i).displayPath();
            }
            //iterate through each path entry in the successfullPaths array, count the cost and time of each path and store it in the path object
            for (int i = 0; i < successfullPaths.size(); i++) {
                //iterate through each city in the path
                for(int j = 0; j < successfullPaths.get(i).getPath().size() - 1; j++) {
                    //iterate through each flight in the adjacency list of the current city
                    for(Flight flight : flightInfo.get(successfullPaths.get(i).getPath().get(j))) {
                        //if the destination of the flight is the next city in the path
                        if(flight.destination.equals(successfullPaths.get(i).getPath().get(j + 1))) {
                            //add the cost of the flight to the cost or time of the path
                            successfullPaths.get(i).addCost(flight.cost);
                            //add the time of the flight to the cost or time of the path
                            successfullPaths.get(i).addTime(flight.time);
                        }
                    }
                }
            }
            //display paths again with cost or time for testing purposes
            for (int i = 0; i < successfullPaths.size(); i++) {
                System.out.println("Path " + (i + 1) + ": ");
                successfullPaths.get(i).displayPath();
                System.out.println("Cost: " + successfullPaths.get(i).getCost());
                //display time
                System.out.println("Time: " + successfullPaths.get(i).getTime());

            }

            //now based on the user input, we sort the successfullPaths array by cost or time
            //once we find the top 3 shortest time/cost paths, we output them to the file

            //create array list of size 3 to store the top 3 shortest time/cost paths using selection sort
            if(costOrTime.equals("C"))
            {
                //selection sort based on cost, find the top 3 shortest cost paths
                //iterate through each path entry in the successfullPaths array, count cost or
                //time of each path
                for (int i = 0; i < successfullPaths.size(); i++) {
                    //iterate through each city in the path
                    for(int j = 0; j < successfullPaths.get(i).getPath().size() - 1; j++) {
                        //iterate through each flight in the adjacency list of the current city
                        for(Flight flight : flightInfo.get(successfullPaths.get(i).getPath().get(j))) {
                            //if the destination of the flight is the next city in the path
                            if(flight.destination.equals(successfullPaths.get(i).getPath().get(j + 1))) {
                                //add the cost of the flight to the cost or time of the path
                                successfullPaths.get(i).addCost(flight.cost);
                                //add the time of the flight to the cost or time of the path
                                successfullPaths.get(i).addTime(flight.time);
                            }
                        }
                    }
                }
                //display paths again with cost or time for testing purposes
                for (int i = 0; i < successfullPaths.size(); i++) {
                    System.out.println("Path " + (i + 1) + ": ");
                    successfullPaths.get(i).displayPath();
                    System.out.println("Cost: " + successfullPaths.get(i).getCost());
                    //display time
                    System.out.println("Time: " + successfullPaths.get(i).getTime());
    
                }
            }
            else
            {

            }
        }
            //display paths again in cost sorted order again
            for (int i = 0; i < successfullPaths.size(); i++) {
                System.out.println("Path " + (i + 1) + ": ");
                successfullPaths.get(i).displayPath();
                System.out.println("Cost: " + successfullPaths.get(i).getCost());
                //display time
                System.out.println("Time: " + successfullPaths.get(i).getTime());

            }

                


            /* 
            for (int i = 0; i < successfullPaths.size(); i++) {
                // output the flight path in the following format:
                // Flight 1: Dallas, Houston (Time)
                flightPlansOutputFileWriter.write(
                        "Flight " + (i + 1) + ": " + successfullPaths. + ", " + destinationCity + " (" + costOrTime + ")\n");
                // Path 1: Dallas -> Houston. Time: 51 Cost: 101.00
                flightPlansOutputFileWriter.write("Path " + (i + 1) + ": ");
                for (int j = 0; j < successfullPaths.path.size(); j++) {
                    // Dallas -> Houston
                    flightPlansOutputFileWriter.write(successfullPaths.path.get(j));
                    // if not the last city in the path
                    if (j != successfullPath.path.size() - 1) {
                        // Dallas -> Houston.
                        flightPlansOutputFileWriter.write(" -> ");
                    }
                    // if the last city in the path
                    else {
                        // Dallas -> Houston. Time: 51 Cost: 101.00
                        flightPlansOutputFileWriter
                                .write(". Time: " + successfullPath.cost + " Cost: " + successfullPath.cost + "\n");
                    }
                }
            }
            */
            

        }
}


