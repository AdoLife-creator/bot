# PvPToggle Plugin - NovaNetwork

## Özellikleri
- Paper 1.21.4 - 1.21.8 uyumlu
- Oyuncular bağımsız olarak kendi PvP'lerini açıp kapatabilir
- İki oyuncudan biri PvP kapalıysa hasar verilmez

## Kurulum

### Adım 1: Derleme
```bash
cd PvPToggle
mvn clean package
```

### Adım 2: Yükleme
Üretilen `target/PvPToggle.jar` dosyasını sunucunun `plugins/` klasörüne taşı.

### Adım 3: Sunucuyu Yeniden Başlat
```bash
reload confirm
```

## Komutlar

| Komut | Açıklama |
|-------|----------|
| `/pvp` | PvP'yi aç/kapat (toggle) |
| `/pvp on` | PvP'yi aç |
| `/pvp off` | PvP'yi kapat |

## Özellikler

✅ **Bağımsız Yönetim**: Her oyuncu kendi PvP durumunu yönetir  
✅ **Çift Kontrol**: İki oyuncudan biri kapalıysa hasar verilmez  
✅ **Anında Geri Bildirim**: Mesajlar oyuncuya durumu gösterir  
✅ **Hafif ve Hızlı**: Minimum kaynak kullanımı  

## Not
Sunucu restart olunca PvP durumu sıfırlanır.