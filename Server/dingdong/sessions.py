from dingdong.exceptions import *
from dingdong.events.events import *
from dingdong.events.dispatcher import dispatch
from dingdong.models import User

class OnlineUsers:
    users = []

    @staticmethod
    def add(user: User):
        if not OnlineUsers.has(user):
            # The SocketServer which listens for events will start
            # only after the user has SUCCESSFULLY logged in!
            # Hence, the order of the following two lines of code
            # DOES matter. If we reverse their order, the dispatcher
            # will get a connection refused error
            # (since the SocketServer has not yet started) :)
            dispatch(LoginEvent(user), OnlineUsers.users)
            OnlineUsers.users.append(user)
        else:
            raise AlreadyLoggedIn("This user has already logged in.")

    @staticmethod
    def remove(user: User):
        if OnlineUsers.has(user):
            # Remove the user from OnlineUsers list first,
            # so that the server will not dispatch this event to 
            # the logging out user whose SocketServer
            # has already shutdown.
            OnlineUsers.users.remove(user)
            dispatch(LogoutEvent(user), OnlineUsers.users)
        else:
            raise NotLoggedIn("This user has already logged out.")

    @staticmethod
    def update(**kwargs):
        # Fetch the username from the varargs.
        user = [value[1] for value in kwargs.items() if value[0] == "user"][0]

        # Update that user's data.
        for value in kwargs.items():
            if value[0] == "user":
                pass
            else:
                exec("user.{0} = \"{1}\"".format(value[0], value[1]))

        user.save()

    @staticmethod
    def has(user):
        return user in OnlineUsers.users

    @staticmethod
    def get(user: User):
        for u in OnlingUsers.users:
            if u == user:
                return u

    @staticmethod
    def get_all(role: str):
        return [user for user in OnlineUsers.users if user.role == role]

    @staticmethod
    def show():
        print(OnlineUsers.users)
