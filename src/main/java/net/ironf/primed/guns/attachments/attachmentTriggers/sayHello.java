package net.ironf.primed.guns.attachments.attachmentTriggers;
import net.ironf.primed.guns.backened.attachements.attachmentTrigger;

public class sayHello extends attachmentTrigger {

    @Override
    public void execute(Object pass, Object otherPass) {
        System.out.println("Hello!");
    }
}
