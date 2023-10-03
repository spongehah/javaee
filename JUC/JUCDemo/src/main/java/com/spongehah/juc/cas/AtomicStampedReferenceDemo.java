package com.spongehah.juc.cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicStampedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Book{
    Integer id;
    String bookName;
}
public class AtomicStampedReferenceDemo {
    public static void main(String[] args) {
        Book javaBook = new Book(1,"javaBook");
        AtomicStampedReference<Book> stampedReference = new AtomicStampedReference<>(javaBook,1);
        System.out.println(stampedReference.getReference() + "\t" + stampedReference.getStamp());
        
        Book mysqlBook = new Book(2,"mysqlBook");
        boolean b;
        b = stampedReference.compareAndSet(javaBook,mysqlBook,stampedReference.getStamp(),stampedReference.getStamp()+1);

        System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());
    }
}
