from dingdong.exceptions import *
from dingdong.events.events import *
from dingdong.events.dispatcher import dispatch
from dingdong.models import User

class OnlineUsers:
    users = []

    @staticmethod
    def add(user: User):
        if not OnlineUsers.has(user):
            OnlineUsers.users.append(user)
        else:
            raise AlreadyLoggedIn("This user has already logged in.")

    @staticmethod
    def remove(user: User):
        if OnlineUsers.has(user):
            OnlineUsers.users.remove(user)
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
