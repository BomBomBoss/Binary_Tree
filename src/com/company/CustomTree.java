package com.company;

import java.io.Serializable;
import java.util.*;

/*
Построй дерево(1)
*/

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;

    public CustomTree() {
        this.root = new Entry<>("root");
    }

    public String getParent(String s) {
        if(root==null) {
            return "root have no parent";
        }
        else {
            Entry<String> result = root.elementFinder(s);
            if(result==null) return "null";
            return result.parent.elementName;
        }
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren ? true :false;

        }
        public Entry<T> elementFinder(String elementName) {
            Stack<Entry<T>> stack = new Stack<>();
            stack.add(this);


            while(!stack.isEmpty()){
                Entry<T> result = stack.pop();
                if (result.elementName.equals(elementName)) {
                    return result;
                }else {
                    if(result.leftChild!=null) stack.add(result.leftChild);
                    if(result.rightChild!=null) stack.add(result.rightChild);
                }
            } return null;
        }

        public boolean parentRecovery(){
            Queue<Entry<T>> queue = new LinkedList<>();
            queue.add(this);
            Entry<T> elementToCheck;

            while (!queue.isEmpty()) {
                elementToCheck = queue.poll();
                if (!elementToCheck.isAvailableToAddChildren()) {
                    if (elementToCheck.leftChild != null) queue.add(elementToCheck.leftChild);
                    if (elementToCheck.rightChild != null) queue.add(elementToCheck.rightChild);
                    else if(elementToCheck.leftChild==null && elementToCheck.rightChild==null) {
                        elementToCheck.availableToAddLeftChildren = true;
                        elementToCheck.availableToAddRightChildren = true;
                    }
                }
            } return true;
        }


        public void createElement(String elementName) {
            Queue<Entry<T>> queue = new LinkedList<>();
            Entry<T> child = new Entry<>(elementName);
            queue.add(this);

            while(!queue.isEmpty()) {
                Entry<T> element = queue.poll();

                if (!element.isAvailableToAddChildren()) {
                    if(element.leftChild!=null) queue.add(element.leftChild);
                    if(element.rightChild!=null) queue.add(element.rightChild);
                    else if (queue.size()==0) {
                        System.out.println("Element can't be added");
                        parentRecovery();
                        if (parentRecovery()) System.out.println("Parents are recovered");
                        queue.add(this);
                    } else continue;
                }
                else {
                    if(element.leftChild==null && element.availableToAddLeftChildren) {
                        child.parent=element;
                        element.availableToAddLeftChildren=false;
                        element.leftChild = child;
                        break;
                    } else if(element.rightChild==null && element.availableToAddRightChildren) {
                        child.parent = element;
                        element.availableToAddRightChildren = false;
                        element.rightChild = child;
                        break;
                    }
                }
            }
        }

        public int counter() {
            int result = 1;

            if(leftChild!=null) { result += leftChild.counter();}
            if(rightChild!=null) {result+= rightChild.counter();}
            return result;
        }
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(String s) {
        if (root==null) {
            root = new Entry<>(s);
        } else {
            root.createElement(s);
        } return  true;
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        if(!(o instanceof String)) throw new UnsupportedOperationException();
        String name = (String) o;
        Entry<String> elementToRemove = root.elementFinder((String) o);
        if(elementToRemove==root ) {
            System.out.println("Element can't be deleted");
            return false;
        }
        Stack<Entry<String>> stack = new Stack<>();
        stack.add(elementToRemove.parent);

        elementToRemove = stack.pop();
        if(elementToRemove.leftChild!=null &&(elementToRemove.leftChild.elementName.equals(name))) {
            stack.add(elementToRemove.leftChild);
            elementToRemove.leftChild = null;
        }
        else {
            stack.add(elementToRemove.rightChild);
            elementToRemove.rightChild = null;
        }

        do {
            elementToRemove = stack.pop();
            elementToRemove.parent = null;
            if(elementToRemove.leftChild!=null) {
                stack.add(elementToRemove.leftChild);
                elementToRemove.leftChild = null;
            }
            if(elementToRemove.rightChild!=null) {
                stack.add(elementToRemove.rightChild);
                elementToRemove.rightChild = null;
            }

        } while (!stack.isEmpty());

        return true;

    }



    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        if (root==null) return 0;

        else  {
            int result = 0;
            result = root.counter() -1;
            return result;
        }
    }
}
