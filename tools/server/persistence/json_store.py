"""Atomic JSON file persistence for game state."""
import json
import os
import shutil
import tempfile
from pathlib import Path


class JSONStore:
    """Thread-safe JSON file persistence with atomic writes."""

    def __init__(self, data_dir: str):
        self.data_dir = Path(data_dir)
        self.data_dir.mkdir(parents=True, exist_ok=True)

    def _player_path(self, player_id: str) -> Path:
        safe_id = player_id.replace("/", "_").replace("\\", "_")
        return self.data_dir / f"{safe_id}.json"

    def load(self, player_id: str, default: dict = None) -> dict:
        path = self._player_path(player_id)
        if not path.exists():
            return default.copy() if default else {}
        with open(path, "r") as f:
            return json.load(f)

    def save(self, player_id: str, data: dict):
        path = self._player_path(player_id)
        tmp_fd, tmp_path = tempfile.mkstemp(
            dir=self.data_dir, suffix=".tmp"
        )
        try:
            with os.fdopen(tmp_fd, "w") as f:
                json.dump(data, f, indent=2, ensure_ascii=False)
            shutil.move(tmp_path, path)
        except Exception:
            try:
                os.unlink(tmp_path)
            except OSError:
                pass
            raise

    def exists(self, player_id: str) -> bool:
        return self._player_path(player_id).exists()

    def delete(self, player_id: str) -> bool:
        path = self._player_path(player_id)
        if path.exists():
            path.unlink()
            return True
        return False
