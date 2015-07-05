package com.gbtec.eclipse.markers;

public class A {
    private B service = new B();
    
    private int add(int a, int b) {
        return this.annotatedMethod();
    }
    
    private void serviceCall() {
        service.getUserById();
    }

    @Deprecated
    private int annotatedMethod() {
        return 2;
    }
}
