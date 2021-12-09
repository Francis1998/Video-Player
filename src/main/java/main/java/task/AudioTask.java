package main.java.task;

import main.java.data.DataManager;
import main.java.presenter.display.PrimaryVideoDisplayPresenter;

public class AudioTask implements Runnable{
    PrimaryVideoDisplayPresenter mPresenter;

    public AudioTask(PrimaryVideoDisplayPresenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public void run() {
        int cur_off = 0;
        int len = DataManager.getInstance().audio_data.length;

        int off = 0;
        int step = DataManager.getInstance().bytes_per_video_frame * 15;

        while (true) {
            if (mPresenter.isJumped) {
                DataManager.getInstance().initAudio();
                DataManager.getInstance().audio_video_offset = mPresenter.jump_event.targetFrame - 1;
                cur_off = (mPresenter.jump_event.targetFrame - 1) * (DataManager.getInstance().bytes_per_video_frame / 4);
                off = 0;
                mPresenter.isJumped = false;
            }

            if (mPresenter.isStopped) {
                DataManager.getInstance().audio_play_line.close();
                DataManager.getInstance().audio_video_offset = 0;
                cur_off = 0;
                off = 0;
                mPresenter.isStopped = false;
            }

            if (mPresenter.isPause) {
                DataManager.getInstance().audio_play_line.stop();
            } else {
                if (!DataManager.getInstance().audio_play_line.isOpen()) {
                    try {
                        DataManager.getInstance().audio_play_line.open(DataManager.getInstance().audioFormat);
                    } catch (Exception e) {
                        System.out.println("Exception thrown:" + e);
                    }
                }
                DataManager.getInstance().audio_play_line.start();
                if (cur_off + off < len) {
                    DataManager.getInstance().audio_play_line.write(DataManager.getInstance().audio_data, cur_off + off, step);
                    DataManager.getInstance().audio_play_line.drain();
                    off += step;
                }
            }
        }
    }
}
