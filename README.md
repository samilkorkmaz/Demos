This repo contains demo files/projects for different problems and concepts.

<b>ThreadDemo.java</b> demonstrates how to make a thread wait and how to wake it up (notify). For further info see <a href= "http://www.youtube.com/watch?v=gx_YUORX5vk">Advanced Java: Multi-threading Part 8 - Wait and Notify</a>

<b>TestPointEquals.java</b>: The equals() method overriden in java.awt.Point class does not check object reference but x, y values. This can create problems when removing points from a list because the ArrayList.remove() method removes the first object in list that "equals" the input object. In the case of a Point object, that means equal x, y coordinates. For example, when you have two Point objects with same x, y in list, there is no way to remove the second object from list by using remove(Object). What you can do is to create a new class that extends Point and overrides equals() so that it checks object reference.
