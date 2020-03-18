# DataPivot
This is a small scala script that reads a provided CSV file and allows you to pivot the CSV file based on your chosen column name. Wrote this script to test out pivoting data as there was a need to create something similar for a different project

You can run this programme if you have SBT to compile and build.

<b>How it's done</b></br>
The script stores each line in the CSV file into a list. Based on the user's input the script tries to match the the input with the header of the file. After filtering out the other elements in the whole list, we keep the resulting results into case classes/arrays and do a count on the total number of occurence.
