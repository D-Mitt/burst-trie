# burst-trie
Report on Efficient Auto-Completion Using Burst Tries

An implementation of a burst trie, with a report on the speed of using this in a dictionary look-up environment compared to other types of containers.

A burst trie is based on a un-balanced tree structure, and nodes can contain either arrays of nodes or a single node. The parameters of when arrays are broken up into new levels of the tree can be adjusted to experiment with the best configuration for the situation. 

Must first be populated with words.
