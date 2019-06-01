from django.db import models
from django.utils import timezone
from os.path import dirname

class User(models.Model):
    username = models.CharField(max_length=20, primary_key=True)
    password = models.CharField(max_length=64)
    fullname = models.CharField(max_length=20)
    avatar = models.BinaryField()
    last_login_ip = models.CharField(max_length=16, null=True, blank=True)
    friends = models.CharField(max_length=5000, null=True, blank=True) # encoded as a json array

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return self.fullname


class Message(models.Model):
    source_ip = models.CharField(max_length=16)
    source_user = models.ForeignKey('User', related_name="source_user")
    target_user = models.ForeignKey('User', related_name="target_user")
    content = models.CharField(max_length=256)
    is_read = models.BooleanField(default=False)

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return "[%s->%s] %s".format(
            self.source_user.username, self.target_user.username, self.content)
