# Warehouse VRP

This repository contains a solution for the Vehicle Routing Problem (VRP) focused on optimizing the routes of robots in a warehouse environment. The robots are tasked with moving boxes between various locations, with the objective of planning routes as efficiently as possible. This solution was developed as part of the course **Algorithms for Decision Support** at KU Leuven 

# Run the jar

The first argument is the file location with the needed data. The second argument is a boolean if you want more nuanced details about what the vehicles do.

True:

```
14: Vehicle0 has picked up box B9 on location stack0_1
129: Vehicle0 has dropped off box B9 on location BufferPoint
254: Vehicle0 has picked up box B14 on location stack0_2
379: Vehicle0 has dropped off box B14 on location BufferPoint
504: Vehicle0 has picked up box B13 on location stack0_2
629: Vehicle0 has dropped off box B13 on location BufferPoint
754: Vehicle0 has picked up box B12 on location stack0_2
879: Vehicle0 has dropped off box B12 on location BufferPoint
```

False:

```
Vehicle0;100;100;0;110;100;15;B9;PU
Vehicle0;110;100;15;0;100;130;B9;PL
Vehicle0;0;100;130;120;100;255;B14;PU
Vehicle0;120;100;255;0;100;380;B14;PL
Vehicle0;0;100;380;120;100;505;B13;PU
Vehicle0;120;100;505;0;100;630;B13;PL
Vehicle0;0;100;630;120;100;755;B12;PU
Vehicle0;120;100;755;0;100;880;B12;PL
```

Example command:

```
java -jar Project.jar testData/I3_3_1.json false
```
