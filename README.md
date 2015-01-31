This repo contains demo files/projects for different problems and concepts.

<b>ThreadDemo.java</b> demonstrates how to make a thread wait and how to wake it up (notify). For further info see <a href= "http://www.youtube.com/watch?v=gx_YUORX5vk">Advanced Java: Multi-threading Part 8 - Wait and Notify</a>

<b>TestPointEquals.java</b>: The equals() method overriden in java.awt.Point class does not check object reference but x, y values. This can create problems when removing points from a list because the ArrayList.remove() method removes the first object in list that "equals" the input object. For example, when you have two Point objects with same x, y in list, there is no way to remove the second object from list by using remove(Object), it will always remove the first object because x, y of first is equal to x, y of second. What you can do is to create a new class that extends Point and overrides equals() so that it checks object reference.

<b>SetListTest.java</b>: Check speed of TreeSet and ArrayList. Adds items to set and list, then finds the item with the lowest cost. ArrayList is an order of magnitude faster.

<b>UpdateGUIFromAnotherThread.java</b>: Demonstrates how to update GUI from another thread. A thread increments the counter and GUI displays it in the form of a progress bar.
