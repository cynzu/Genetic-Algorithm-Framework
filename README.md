# Genetic-Algorithm-Framework
This framework which allows a programmer to use all the wonders of genetic algorithms simply by providing a fitness tester. It was written in 2001-2003 in java.

<html>
<head>
<link type="text/css" href="../examples.css" rel="stylesheet">
</head>

<body bgcolor="#FFFFFF">

<a name="top" />
<h2>Genetic Algorithm Framework and Sample Client</h2> 
<p>
The zujkomiller.genetic package and its subpackages contain a framework of classes written in java for performing genetic algorithms.  The zujkomiller.maze package and its subpackages contain a client of that framework which uses genetic algorithms to find the best solution to a maze.  Below is a detailed description of these projects, but feel free to jump right to the <a href="#code">sample code and output files</a>, <a href="#uml">uml diagrams</a>, <a href="#shots">screen shots</a>, and <a href="#downloads">downloads</a>. 
</p>

<h3>Brief Introduction to Genetic Algorithms</h3>
<p>
A genetic algorithm mimics the process of biological evolution in order to find a solution to a problem.  In nature, the "problem" is survival, and the "solution" is to create individuals with traits which enable them to survive and reproduce, thereby passing on the genes which make them fit. In nature, chromosomes of DNA molecules carry the genes which become expressed as traits.  In a computer program the "problem" can be anything, and the program designer can choose what it means for a solution to be "good" or "fit".
</p>
<p>
As in nature, a genetic algorithm deals with chromosomes.  These are usually strings of characters, or "genes".  In DNA, they are A, T, C, and G, but in a genetic algorithm they may be either binary (containing only ones and zeros), or more complex (containing more than two symbols in its alphabet of possible genes).  Just as in nature, a genetic algorithm will start with one "generation" or set of chromosomes each of which is generated randomly, each with a slightly different configuration.  Then, each individual in that generation is put through some test of fitness which the programmer devises.  The fitness test should reflect how well the solution (represented by the chromosome) solves the problem.  After each individual in a parent generation is tested for fitness, a child generation is created from the parent: the least fit members of the parent generation are thrown out; the most fit members are cloned and "mated" with one another; and a few random mutations are added to the mix. After many generations, better and better solutions to the problem emerge because the genes that make them good solutions are selected-for through this process of creating child generations from the most fit members of the parent generation.
</p>

<h3>Genetic Algorithm Framework</h3>
<p>
The Genetic Algorithm Framework in the zujkomiller.genetic package contains classes for performing the basic steps of a genetic algorithm: creating initial populations of chromosomes, testing for fitness, and creating child generations from the parents through replication, mutation, and crossovers.  It is a flexible framework which allows clients to perform genetic algorithms on many types problems, using any fitness test the client can produce.
</p>
<h4>Types of Chromosomes</h4>
<p>
The most basic type of Chromosome is linear, represented by a string of characters. For some problems, it is appropriate to tell the framework to create Chromosomes which are sets, having no duplicate genes.  For other problems, it would be appropriate to have a "bag" which may contain duplicate genes. In some problems, the Chromosomes should always be of a fixed length, but for others the Chromosomes in a generation may be of varying length. For this reason, the Genetic Algorithm Framework is capable of performing operations on the following types of Chromosomes:
<ul>
    <li>
        Fixed Set Chromosomes: duplicate genes are not allowed, and Chromosomes are always of a given length 
        <br>
        ex: {a, b, c, d, e} and {b, c, d, a, e} are two legal Fixed Set Chromosomes

    </li>
    <li>
        Fixed Bag Chromosomes: duplicate genes are allowed, and Chromosomes are always of a given length
        <br>
        ex: {a, b, b, d, a} and {c, c, a, e, c} are two legal Fixed Bag Chromosomes
    </li>
    <li>
        Variable Set Chromosomes: duplicate genes are not allowed, and Chromosomes may be of varying lengths within a given range
        <br>
        ex: {a, b, c, d, e} and {d, a, e} are two legal Variable Set Chromosomes
    </li>
    <li>
        Variable Bag Chromosomes: duplicate genes are allowed, and Chromosomes may be of varying lengths within a given range
        <br>
        ex: {a, b, b, d, a} and {c, a} are two legal Variable Bag Chromosomes
    </li>
</ul>
</p>

<h4>Classes in the Genetic Algorithm Framework</h4>
Below are descriptions of some of the central classes in the framework.  For complete descriptions of all the classes, please see the <a href="./doc/index.html" target="_blank">Developers' Documentation</a> and the <a href="./images/classDiagram.jpg" target="_blank">Class Diagram</a>.

<p>
<b>Genie</b> is the main entry point to the Genetic Algorithm Framework.  It uses values from a .properties file passed in as a parameter to configure the framework to run the algorithm.  The .properties file contains information such as which type of Chromosomes to create, the number of individuals in a generation, the total number of generations to create, and which implementation of FitnessTester to use when testing Chromosomes for fitness. See <a href="./samples/MazeGenetic.txt" target="_blank">MazeGenetic.properties</a> for an example of this file.
</p>
<p>
<b>Evolver</b> controls the execution of the genetic algorithm. It implements the EvolutionObservable interface so that classes implementing EvolutionObserver may register themselves with it and be notified when new generations are created and tested for fitness.  The Evolver uses a ProducerFactory to find the correct GenerationProducer needed to create each generation.  (There are different GenerationProducers for each type of Chromosome listed above.)  After a generation has been created, the Evolver passes it to an implementation of the FitnessTester interface which assigns fitness scores to each individual Chromosome in the generation.
</p>
<p>
<b>ProducerFactory</b> contains logic for finding the appropriate subclass of GenerationProducer to use for the current genetic algorithm. 
</p>
<p>
<b>GenerationProducer</b> is an abstract class which defines the template for producing generations: replicate the best Chromosomes from the parent generation and eliminate the worst ones, then perform crossovers which mate the best Chromosomes with one another, and finally introduce random mutations. 
</p>
<b>Fitness Tester</b> is an interface defining the method which assigns a fitness score to an individual Chromosome.
</p>
<b>Chromosome</b> is an interface defining the required methods of any Chromosome, or collection of genes.
</p>

<h3>Sample Client: Using Genetic Algorithms to Solve a Maze</h3>
<p>
One client of the Genetic Algorithm Framework uses genetic algorithms to solve a maze.  Mazes consist of a simple grid, with a starting point, an ending point, and walls along the way.  The <a href="./samples/MazeGenetic.txt" target="_blank">MazeGenetic.properties</a> file configures the framework to create Variable Bag Chromosomes which represent taking steps to the North, South, East, or West.  It provides a fitness tester which determines how close the Chromosome came to solving the maze, subtracting points for each time a wall is hit, and adding bonus points if the end-point was reached.  See the <a href="./samples/difficult.txt" target="_blank">output</a> to view the results of running this genetic algorithm in plain text.  See the <a href="#shots">screen shots</a> listed below to view these results in the Results Displayer graphical user interface.
</p>
<p>
The Results Displayer graphical user interface allows the user to see the static path that was taken by each Chromosome in an attempt to solve the maze. The panel on the left side of the screen allows the user to navigate through each generation. The individuals in a generation are grouped by their fitness score. To see the path that each individual created, the user double-clicks on the solution in the left pane. This brings up a picture of the maze and a red line showing the path created by the individual. Below the picture of the maze, there are buttons which allow the user to either walk through the solution one grid at a time (by following a red dot that travels along the red path), or to see the path animated.  At the bottom of that pane, the "Directions Traveled" section lists each "gene" in the chromosome, or each direction to travel, starting at the starting-point of the maze. When viewing the <a href="#shots">screen shots</a>, note that individuals with higher scores come closer to solving the maze efficiently. 
</p>

<a name="code" />
<h3>Sample Code and Files</h3>
<ul>
    <li>
        <a href="./samples/GenerationProducer.txt" target="_blank">GenerationProducer.java</a>
    </li>
    <li>
        <a href="./samples/VariableBagProducer.txt" target="_blank">VariableBagProducer.java</a>
    </li>
    <li>
        <a href="./samples/MazeSolutionDisplayer.txt" target="_blank">MazeSolutionDisplayer.java</a>
    </li>
    <li>
        <a href="./samples/MazeGenetic.txt" target="_blank">MazeGenetic.properties</a> used to configure the Genetic Algorithm Framework to solve a maze
    </li>
    <li>
        <a href="./samples/difficult.txt" target="_blank">Output</a> from solving a maze with the Genetic Algorithm Framework
    </li>
    <li>
        <a href="./doc/index.html" target="_blank">Developers' Documentation</a>
    </li>
    <li>
        The complete source code is available from the <a href="#downloads">Downloads</a> section below.
    </li>
</ul>

<a name="uml" />
<h3>UML</h3>
<ul>
    <li>
        <a href="./images/componentView.jpg" target="_blank">Component View</a>
    </li>
    <li>
        <a href="./images/classDiagram.jpg" target="_blank">Class Diagram</a>
    </li>
</ul>

<a name="shots" />
<h3>Screen Shots</h3>
<ul>
    <li>
        <a href="./images/gen1score42.gif" target="_blank">Generation 1, score of 42</a>
    </li>
    <li>
        <a href="./images/gen20score48.gif" target="_blank">Generation 20, score of 48</a>
    </li>
    <li>
        <a href="./images/gen50score142.gif" target="_blank">Generation 50, score of 142</a>
    </li>
</ul>

<a name="downloads" />
<h3>Downloads</h3>
<p>
You may download the <a href="./deploy/maze.zip">maze.zip</a> file to run the program.  It contains a directory called maze, with subdirectories: bin, lib, output, and storage. (Make sure your classpath variable is updated to point to the maze.jar and genetic.jar in the maze/lib directory.) Once you've unzipped the contents, open a command prompt and change directories to the maze/bin directory.  To solve a maze with the genetic algorithm, run the solvemaze.bat file at the command prompt.  (There is currently no gui for setting up the parameters and running the algorithm, only a gui for viewing the results.)  To view the results of your run in plain text, go to the maze/output directory and open the "difficult.txt" file.  To use the graphical user interface to view your results, run the maze.bat file from a command prompt.  From the tools menu select "Results Displayer" (the other two tools are works-in-progress, so won't do you any good). Then select File/Open.  A pop-up window will appear with all of the maze solutions whose names include a timestamp of when they were run.  Select one of these and hit OK.  This will open the tree-view of generations in the left pane.  Navigate through this, double-clicking on individual solutions to view the path they took in their attempt to solve the maze.
</p>
<p>
You may also download the source code, which I'm distributing under the GNU General Public License, as free, open source software. 
<ul>
    <li>
        <a href="./src/GnuGPL.txt" target="_blank">GNU General Public License</a>
    </li>
    <li>
        <a href="./src/zujkomiller.zip">source code</a>
    </li>
</ul>
</p>

</body>
</html>
