package application;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class MainController {
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Pane pane;
	@FXML
	private Slider barra;
	@FXML
	private Slider barraVolume;
    @FXML
    private ListView <File> listaMusica;
	@FXML    
	private Label lblNmusica, lblTitulo,lblTempo, lblDuracao;
	@FXML
	private Button btnPlay, btnPause, btnAvancar, btnVoltar;
	private Duration resumePlayer;
	private ArrayList<File> musica;
	private File directory;
	private File[] files;
	private Timer timer;
	private MediaPlayer danp;
	private Media mp;
	private boolean ok = false;
	private static String CaminhoMusica;
	int musicaNumero;

	//@Override
	public void inicialize() {		
		musica = new ArrayList<File>();		
		directory = new File("music");		
		files = directory.listFiles();		
		if(files != null) {			
			for(File file : files) {				
				musica.add(file);
			
			}
		}
		
		mp = new Media(musica.get(musicaNumero).toURI().toString());
		danp = new MediaPlayer(mp);		
		lblTitulo.setText(musica.get(musicaNumero).getName());
		play();
		//DuracaoTotal();
	}

	public void EscolhaCaminho() {
		try {
			FileChooser fc = new FileChooser();
			fc.setTitle("Escolha uma musica");
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3*"));
			File file = fc.showOpenDialog(null);
			
		if (file != null) {
			lblTitulo.setText(file.getName().replace("MP3", "*.mp3*"));
			CaminhoMusica = file.getAbsolutePath();
			mp = new Media(Paths.get(CaminhoMusica).toUri().toString());
			danp = new MediaPlayer(mp);
			play();
			//BarraAvancar();
		   
			//DuracaoTotal();
			
		} 
		}catch(NullPointerException e) {
		
		}catch(Exception e) {
			MSA("Arquivo Invalido");
		}
	}
	
	public void acaoPlayList() {
		
		pane.setVisible(true);
	
}
	
	public void fechaLista() {
		pane.setVisible(false);
	}
	
	public void play() {
		try {
		if (ok == false) {
			
			danp.play();
			ok = true;
			retorna();
			BarraAvancar();
			reloop();
			DuracaoTotal();
			Temporizador();
			volume();
			
		
		}

	}catch(NullPointerException e){
		MSA("Escolha alguma música!");
	}
}
	public void cancelaBarra() {
		ok = false;
		timer.cancel();
	}

	public void pause() {
		try {
		cancelaBarra();
		danp.pause();
		ok =false;
	}catch(NullPointerException e) {
		MSA("Escolha uma música e aperte o play!");
	}
}
	public void voltar() {
		try {
		if (musicaNumero > 0) {
			musicaNumero--;
			danp.stop();
			if (ok) {
				cancelaBarra();
			}
			mp = new Media(musica.get(musicaNumero).toURI().toString());
			danp = new MediaPlayer(mp);
			lblTitulo.setText(musica.get(musicaNumero).getName());
			play();
		} else {
			musicaNumero = musica.size() - 1;
			danp.stop();
			if (ok) {
				cancelaBarra();
			}
			mp = new Media(musica.get(musicaNumero).toURI().toString());
			danp = new MediaPlayer(mp);
			lblTitulo.setText(musica.get(musicaNumero).getName());
			play();
		}
	}catch(NullPointerException e) {
		MSA("Não há músicas para voltar ");
	}
	}
	public void avancar() {
		try {
		if (musicaNumero < musica.size() - 1) {
			musicaNumero++;
			danp.stop();
			if (ok) {
				cancelaBarra();
			}
			mp = new Media(musica.get(musicaNumero).toURI().toString());
			danp = new MediaPlayer(mp);
			lblTitulo.setText(musica.get(musicaNumero).getName());
			play();
		} else {
			musicaNumero = 0;
			danp.stop();
			mp = new Media(musica.get(musicaNumero).toURI().toString());
			danp = new MediaPlayer(mp);
			lblTitulo.setText(musica.get(musicaNumero).getName());
			play();
		}
	}catch(NullPointerException e) {
		MSA("Não há músicas para avançar");
	}
	}
	public void volume() {
		barraVolume.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {

	    	 @Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				danp.setVolume(barraVolume.getValue() * 0.01);
			}
		});
		barra.setStyle("-fx-accent: #00FF00;");
		
		
	
	}

	public void BarraAvancar() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				double currentSeconds = danp.getCurrentTime().toSeconds();
				double end = mp.getDuration().toSeconds();
				barra.setMax(end);
				barra.setValue(currentSeconds);

			}
		}, 0, 1000);
	}
	
	public void MSA (String k){
		Alert alert = new Alert (AlertType.WARNING);
		alert.setTitle("Atenção");
		alert.setContentText(k);
		alert.setHeaderText(null);
		alert.showAndWait();
		
		}
	
	public void SliderTime() {
		new Thread(() -> 
		danp.seek(Duration.seconds(barra.getValue()))).start();
			}
	
	public void Temporizador() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					int Time = (int) danp.getCurrentTime().toMillis();
					int minutes = (Time / 60000) % 60000;
					int seconds = Time % 60000 / 1000;
					String CurrentTime = String.format("%02d:%02d", minutes, seconds);
					lblTempo.setText(CurrentTime);
				});

			}
		}, 0, 1000);
	}
	
	public void DuracaoTotal() {
		danp.setOnReady(new Runnable() {
			
			@Override
			public void run() {
				int tempoDuracao = (int) mp.getDuration().toMillis();
				int minutos = (tempoDuracao / 60000) % 60000;
				int segundos = tempoDuracao % 60000 / 1000;
				String CurrentTime = String.format("%02d:%02d", minutos, segundos);
				lblDuracao.setText(CurrentTime);
				
				int Tempo = (int) danp.getCurrentTime().toMillis();
				int minutosAnda = (Tempo / 60000) % 60000;
				int segundosAnda = Tempo % 60000 / 1000;
				String tempoAnda = String.format("%02d:%02d", minutosAnda, segundosAnda);
				lblTempo.setText(tempoAnda);
				
			}
		});
	}
	
	public void retorna() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(() -> {
				if(resumePlayer != null && ok == true) {
					danp.setStartTime(resumePlayer);
				}else {
					resumePlayer = danp.getCurrentTime();
				}
				});
			}
		},0,1000);
	}
	
	public void reloop() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				Platform.runLater(() -> { 
				try {
					  if(danp.getCurrentTime().toSeconds() == mp.getDuration().toSeconds()) {
							resumePlayer = null;
							danp.setStartTime(resumePlayer);
							play();
						}
					  }catch(Exception e ) {}
				  });
				 
				  } }, 0, 1000);	
	}
	
	public void Starway () {
		if(ok == true) {
			play();
			EscolhaCaminho();;
		}else {
			EscolhaCaminho();
		}
	}
	
	
}
