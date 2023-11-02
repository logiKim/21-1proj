from flask_restx import Namespace

Equip = Namespace(name='equip', description='운동기구 관련 API') #import

# API Import
from . import e_p_list #운동기구 이름-> 해당 운동기구가 있는 공원들의 위치 list를 얻어오는 파일