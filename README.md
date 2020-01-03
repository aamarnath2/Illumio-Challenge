# Illumio-Challenge
Illumio internship coding challenge

# Testing
I initially tested my code using the sample dataset and sample acceptPacket calls. Once I determined that my code was working for the given cases, I decided to test edge cases. Such edge cases included adding a rule that had an IP address range as well as a port number range, adding a rule at the beginning/end of the valid port/IP address range (i.e. port num = 1 or 65535, ip_address = 0.0.0.0 or 255.255.255.255).For more formal testing, I could have used JUnit; however, for the purpose of this coding challenge I decided not to. 

 
# Design and Implementation
Given that the guidelines and requirements indicate that code should work "reasonably quickly" AFTER the dataset has been loaded, I determined that optimizing the time complexity of the acceptPacket function would be the main priority. As a result, there was  a tradeoff for space complexity. I determined that using a hash map to store all the rules for a network would be the best way to optimize the acceptPacket function as it would enable O(1) time complexity. In order to build this map, I decided to create a hashCode for each rule - this hashCode was comprised of the sum of the port number, IP address number (long), hashed direction, and hashed protocol. As mentioned, the major tradeoff/downside is the space complexity, as data expressed using ranges (ip or port) is broken down into individual Rules.

# Optimizations
One potential optimization/refinement would be using a bloom filter (although the probabilistic nature of such a data structure would only allow us to determine whether a rule doesn't exists or maybe exists). To reduce space complexity, the possibility of merging rules (I.e. same direction and protocol and overlapping ip address and port number intervals) could be implemented. In a similar vein, I believe that a different data structure that is better suited to storing ranges/intervals (like a interval tree or segment tree) might have been used to reduce space complexity. Additionally, I would have liked to use a more formal testing method such as Unit.

# Rankings
1. Platform team
2. Policy team
