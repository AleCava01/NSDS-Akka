# Actor Model - Akka Lab

**Course:** Actor Model - Akka Lab  
**Institution:** Politecnico di Milano  
**Instructor:** Prof. Luca Mottola ([luca.mottola@polimi.it](mailto:luca.mottola@polimi.it))  
**Website:** [mottola.faculty.polimi.it](https://mottola.faculty.polimi.it)

---

## Table of Contents
- [Exercise 1: Counter Actor with Message Selector](#exercise-1-counter-actor-with-message-selector)
- [Exercise 2: Non-Negative Counter with Stash](#exercise-2-non-negative-counter-with-stash)
- [Exercise 3: Contact List Server & Client](#exercise-3-contact-list-server--client)
- [Exercise 4: Bounded Buffer Server (Sleep/Wakeup)](#exercise-4-bounded-buffer-server-sleepwakeup)
- [Exercise 5: Fault Tolerance & Supervision Strategies](#exercise-5-fault-tolerance--supervision-strategies)
- [Exercise 6: Asynchronous Processing & State Switching](#exercise-6-asynchronous-processing--state-switching)

---

## Lab Exercises (Part 1)

### Exercise 1: Counter Actor with Message Selector
Modify the `CounterActor` code to accept different messages that either increment or decrement the counter.

* **Requirements:**
  * Try both approaches:
    1. Using different **classes of messages**.
    2. Using **message selectors** that predicate on the content of messages.
* **Testing:**
  * Duplicate the line `exec.submit()` to increment or decrement the counter within the `for` loop.

---

### Exercise 2: Non-Negative Counter with Stash
Modify either solution to Exercise 1 to make sure that the state of the `Counter` actor is never negative.

* **Requirements:**
  * If a message is received that would decrement the counter when the state is zero, the message shall be processed later.
  * To use `stash()`, extend your actor from `AbstractActorWithStash`.
* **Testing:**
  * Verify the execution sequence:
    1. `decrement`
    2. `decrement`
    3. `increment`
    4. `decrement`
    5. `increment`
    6. `increment`
  * The actor state should evolve as: `1, 0, 1, 0, 1, 0`.

---

### Exercise 3: Contact List Server & Client
Write a server actor that manages a contact list associating email addresses to names.

* **Server Actor:**
  * Processes two types of messages:
    * `PutMsg`: Adds a new contact to the list.
    * `GetMsg`: Reads the email address of a contact given its name.
* **Client Actor:**
  * Sends messages to the server actor.
  * In the case of a `GetMsg`, expects a reply and displays the content on the console.

---

## Lab Exercises (Part 2)

### Exercise 4: Bounded Buffer Server (Sleep/Wakeup)
Write a server actor that receives text messages and sends them back to the client actor.

* **Behavior:**
  * **Upon receiving a `Sleep` message:**
    * Stores subsequent messages.
    * Stops sending messages back to the client.
  * **Upon receiving a `Wakeup` message:**
    * Sends accumulated messages back to the client in the exact order they were received.
    * Resumes sending new incoming messages directly back to the client.

---

### Exercise 5: Fault Tolerance & Supervision Strategies
Modify the solution to Exercise 3 so that the server actor fails every time a `PutMsg` is received with the name `"Fail!"`.

* **Requirements:**
  * Raise a generic `Exception` when `"Fail!"` is received.
  * Create a **Supervisor Actor** that manages the fault.
  * Verify that:
    * A **Resume strategy** preserves the contact list state.
    * A **Restart strategy** effectively resets the contact list state.

---

### Exercise 6: Asynchronous Processing & State Switching
Write a client actor that asks a server actor to uppercase one `TextMsg` at a time.

* **Server Behavior:**
  * Replies after a short delay that emulates long processing.
  * While processing, the server enters a **"busy"** behavior and buffers new requests arriving in the meantime. *(It must NOT block while processing)*.
  * When processing completes, the server sends the reply to the client, becomes ready again, and processes buffered messages.
* **Testing:**
  * Create two clients issuing a series of commands in parallel.
* **Implementation Hint:**
  * Use `getContext().system().scheduler().scheduleOnce()` for the delay:
    ```java
    getContext().system().scheduler().scheduleOnce(
        delay, 
        recipient, 
        msg, 
        getContext().dispatcher(), 
        sender
    );
    ```