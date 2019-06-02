from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from dingdong.events.dispatcher import *
from dingdong.events.events import *
from dingdong.status_code import StatusCode
from dingdong.models import *

import errno
from socket import error as socket_error

import json

def create(request, source_username):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        target_username = req_body["targetUsername"]
        content = req_body["content"]

        source_user = User.objects.get(username=source_username)
        target_user = User.objects.get(username=target_username)
        source_ip = source_user.last_login_ip

        Message.objects.create(
            source_ip = source_ip,
            source_user = source_user,
            target_user = target_user,
            content = content
        )
        dispatch(NewMessageEvent(source_user, target_user, content), [target_user])
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except Exception as e:
        print(e)
    #except socket_error as serr:
    #    if serr.errno == errno.ECONNREFUSED:
    #        print("user not online, but message has been written to database.")
    #    else:
    #        raise serr # throw this error if this error != connection refused
    return JsonResponse(response_data)


def get(request, username1, username2):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        messages = list(Message.objects.filter(source_user=username1, target_user=username2))
        messages += list(Message.objects.filter(source_user=username2, target_user=username1))
        messages.sort(key=lambda m: m.create_time)

        response_data["content"] = {
            "messages": [{
                "sourceUsername": message.source_user.username,
                "targetUsername": message.target_user.username,
                "content": message.content,
                "isRead": message.is_read
            } for message in messages]
        }
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.NOT_REGISTERED
    return JsonResponse(response_data)
