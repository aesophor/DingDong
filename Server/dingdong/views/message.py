from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from dingdong.events.dispatcher import *
from dingdong.events.events import *
from dingdong.sessions import OnlineUsers
from dingdong.status_code import StatusCode
from dingdong.models import *

import json

def create(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        source_username = req_body["source_username"]
        target_username = req_body["target_username"]
        content = req_body["content"]

        source_user = User.objects.get(username=source_username)
        target_user = User.objects.get(username=target_username)
        source_ip = user.last_login_ip

        Message.objects.create(
            source_ip = source_ip,
            source_user = source_user,
            target_user = target_user,
            content = content
        )

        dispatch(NewMessageEvent(source_user, target_user, content), OnlineUsers.users)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS

    return JsonResponse(response_data)
