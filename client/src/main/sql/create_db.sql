--
-- Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
--                             Research Academic Computer Technology Institute (RACTI)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

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




