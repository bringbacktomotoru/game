"""Handler registry for game commands."""
from typing import Callable, Dict

_handlers: Dict[int, Callable] = {}
_entrance_handlers: Dict[int, Callable] = {}


def register_main(cmd_id: int):
    """Decorator to register a main endpoint handler."""
    def decorator(func):
        _handlers[cmd_id] = func
        return func
    return decorator


def register_entrance(cmd_id: int):
    """Decorator to register an entrance endpoint handler."""
    def decorator(func):
        _entrance_handlers[cmd_id] = func
        return func
    return decorator


def get_main_handler(cmd_id: int) -> Callable | None:
    return _handlers.get(cmd_id)


def get_entrance_handler(cmd_id: int) -> Callable | None:
    return _entrance_handlers.get(cmd_id)


def list_main_handlers() -> list[int]:
    return sorted(_handlers.keys())


def list_entrance_handlers() -> list[int]:
    return sorted(_entrance_handlers.keys())
