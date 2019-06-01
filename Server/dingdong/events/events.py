from dingdong.events.event_type import EventType
from dingdong.models import *

import json


class Event:
    def __init__(self, event_type: EventType):
        self.event_type = event_type


class LoginEvent(Event):
    def __init__(self, user: User):
        super(LoginEvent, self).__init__(EventType.LOGIN)
        self.user = user

    def __str__(self):
        event = {"eventType": self.event_type}

        event["content"] = {
            "username": self.user.username,
            "fullname": self.user.fullname,
        }

        return json.dumps(event)


class LogoutEvent(Event):
    def __init__(self, user: User):
        super(LogoutEvent, self).__init__(EventType.LOGOUT)
        self.user = user

    def __str__(self):
        event = {"eventType": self.event_type}

        event["content"] = {
            "username": self.user.username,
            "fullname": self.user.fullname,
        }

        return json.dumps(event)


class NewMessageEvent(Event):
    def __init__(self, source_user: User, target_user: User, content: str):
        super(NewMessageEvent, self).__init__(EventType.NEW_MESSAGE)
        self.source_user = source_user
        self.target_user = target_user
        self.content = content

    def __str__(self):
        event = {"eventType": self.event_type}

        event["content"] = {
            "source_user": {
                "username": self.source_user.username,
                "fullname": self.source_user.fullname
            },
            "target_user": {
                "username": self.target_user.username,
                "fullname": self.target_user.fullname
            },
            "content": self.content,
        }

        return json.dumps(event)
