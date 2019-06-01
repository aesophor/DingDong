# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(verbose_name='ID', primary_key=True, serialize=False, auto_created=True)),
                ('source_ip', models.CharField(max_length=16)),
                ('content', models.CharField(max_length=256)),
                ('is_read', models.BooleanField(default=False)),
                ('create_time', models.DateTimeField(editable=False)),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('username', models.CharField(primary_key=True, max_length=20, serialize=False)),
                ('password', models.CharField(max_length=64)),
                ('fullname', models.CharField(max_length=20)),
                ('avatar', models.BinaryField()),
                ('last_login_ip', models.CharField(max_length=16, blank=True, null=True)),
                ('friends', models.CharField(max_length=5000, blank=True, null=True)),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
            ],
        ),
        migrations.AddField(
            model_name='message',
            name='source_user',
            field=models.ForeignKey(related_name='source_user', to='dingdong.User'),
        ),
        migrations.AddField(
            model_name='message',
            name='target_user',
            field=models.ForeignKey(related_name='target_user', to='dingdong.User'),
        ),
    ]
