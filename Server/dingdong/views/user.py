from django.core.exceptions import ObjectDoesNotExist
from django.db import IntegrityError
from django.http import JsonResponse

from dingdong.exceptions import AlreadyLoggedIn, NotLoggedIn
from dingdong.status_code import StatusCode
from dingdong.sessions import OnlineUsers
from dingdong.models import *

import json
import base64


def get(request, username):
    response_data = {"statusCode": StatusCode.SUCCESS}
    user = User.objects.get(username=username)

    response_data["content"] = {
        "username": user.username,
        "fullname": user.fullname,
        "avatar": base64.b64encode(user.avatar).decode('ascii'),
        "friends": user.friends
    }
    return JsonResponse(response_data)


def register(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        username = req_body["username"]
        password = req_body["password"]
        fullname = req_body["fullname"]

        User.objects.create(
            username = username,
            password = password,
            fullname = fullname,
            friends = json.dumps([]) # defaults to an empty list of friends
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except IntegrityError:
        response_data["statusCode"] = StatusCode.ALREADY_REGISTERED
    return JsonResponse(response_data)


def login(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        username = req_body["username"]
        password = req_body["password"]
        login_ip = req_body["loginIP"]

        user = User.objects.get(username=username, password=password)
        OnlineUsers.add(user)
        OnlineUsers.update(user=user, last_login_ip=login_ip)
        OnlineUsers.show()
    except AlreadyLoggedIn:
        response_data["statusCode"] = StatusCode.ALREADY_LOGGED_IN
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.VALIDATION_ERR
    return JsonResponse(response_data)


def logout(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        username = req_body["username"]

        user = User.objects.get(username=username)
        OnlineUsers.remove(user)
        OnlineUsers.show()
    except NotLoggedIn:
        response_data["statusCode"] = StatusCode.NOT_LOGGED_IN
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    return JsonResponse(response_data)


def update(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        username = req_body["username"]
        fullname = req_body["fullname"]
        password = req_body["password"]
        avatar = base64.b64decode(req_body["avatar"])

        user = User.objects.get(username=username)
        user.fullname = fullname
        user.password = password
        user.avatar = avatar
        user.save()
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.NOT_REGISTERED
    return JsonResponse(response_data)


def list_friends(request, username):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        friends = json.loads(User.objects.get(username=username).friends)

        response_data["content"] = {
            "friends": []
        }

        for username in friends:
            user = User.objects.get(username=username)
            response_data["content"]["friends"].append({
                "username": user.username,
                "fullname": user.fullname,
                "avatar": base64.b64encode(user.avatar).decode('ascii')
            })
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.NOT_REGISTERED
    return JsonResponse(response_data)


def add_friend(request, source_username):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        target_username = req_body["targetUsername"]

        source_user = User.objects.get(username=source_username)
        target_user = User.objects.get(username=target_username)

        # Add target_user to source_user's friend list.
        source_user_friends = json.loads(source_user.friends)
        if target_username not in source_user_friends:
            source_user_friends.append(target_username)
            source_user.friends = json.dumps(source_user_friends)
            source_user.save()

        # Add source_user to target_user's friend list.
        target_user_friends = json.loads(target_user.friends)
        if source_username not in target_user_friends:
            target_user_friends.append(source_username)
            target_user.friends = json.dumps(target_user_friends)
            target_user.save()
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.NOT_REGISTERED
    return JsonResponse(response_data)
