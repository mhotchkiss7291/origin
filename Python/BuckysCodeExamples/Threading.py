import threading

class MarksMessenger(threading.Thread):
    def run(self):

        # Loop without a variable
        for _ in range(10):
            print(threading.current_thread().getName())

x = MarksMessenger(name='Send out messages')
y = MarksMessenger(name='Receive messages')

# Looks for run() in the class instance
# Starting threads simultaneously. Not consecutive
x.start()
y.start()
