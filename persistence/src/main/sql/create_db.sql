--
-- Create database for WiseUI
-- Author: Sönke Nommensen
-- Date: 02.05.2011
--
-- Database: wiseuidb
-- User:     wiseui
-- Pass:     wiseuipass
--
create database wiseuidb;
--
create user 'wiseui'@'localhost' identified by 'wiseuipass';
--
grant all on wiseuidb.* to 'wiseui'@'localhost';

--
commit;




